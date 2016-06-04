/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.controller;

import static pw.phylame.linyin.util.StringUtils.isEmpty;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pw.phylame.linyin.constants.AccessType;
import pw.phylame.linyin.controller.form.LoginForm;
import pw.phylame.linyin.controller.form.RegisterForm;
import pw.phylame.linyin.domain.Account;
import pw.phylame.linyin.domain.Subscription;
import pw.phylame.linyin.pojo.Tag;
import pw.phylame.linyin.pojo.User;
import pw.phylame.linyin.service.MerchantService;
import pw.phylame.linyin.service.SubscribeService;
import pw.phylame.linyin.service.UserService;
import pw.phylame.linyin.ui.EmptyPager;
import pw.phylame.linyin.ui.Pager;
import pw.phylame.linyin.util.CookieUtils;
import pw.phylame.linyin.util.MiscUtils;
import pw.phylame.linyin.util.MiscUtils.MapBuilder;
import pw.phylame.linyin.util.PropertyMapList;
import pw.phylame.linyin.util.SimpleResponseBuilder;

/**
 * @author Peng Wan
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {
    private static final String MODEL_REGISTER_FORM = "regForm";
    private static final String MODEL_LOGIN_FORM = "loginForm";
    private static final int MAX_LOGIN_TRIES = 7;

    private static final String MODEL_LOGIN_TRIES = "tries";

    @Autowired
    private UserService userService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private ControllerHelper helper;

    private void storeLoggedUser(User user) {
        setSessionAttribute(SESSION_CURRENT_USER, user);
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String initRegister(@ModelAttribute(MODEL_REGISTER_FORM) RegisterForm form, HttpServletRequest req,
            Model m) {
        handleRequest(req, m);
        return performIfLogged(user -> {
            return performText("注销当前用户后再注册账号");
        }, page -> {
            storeSourcePage();
            form.setType(AccessType.WEB);
            return Pages.USER_REGISTER;
        });
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute(MODEL_REGISTER_FORM) RegisterForm form, BindingResult br,
            HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performIfLogged(user -> {
            return performText("注销当前用户后再注册账号");
        }, page -> {
            if (!validateSubmit(form, br)) {
                form.setType(AccessType.WEB);
                return Pages.USER_REGISTER;
            }
            if (!form.checkConfirm()) {
                addFieldError(br, MODEL_REGISTER_FORM, "confirm", "user.register.error.confirmNotMatch");
                form.setType(AccessType.WEB);
                return Pages.USER_REGISTER;
            }
            if (userService.isAccountExist(form.getUsername())) {
                addFieldError(br, MODEL_REGISTER_FORM, "username", "user.register.error.accountExist");
                form.setType(AccessType.WEB);
                return Pages.USER_REGISTER;
            }
            User user = form.dumpToUser();
            userService.registerAccount(form.dumpToAccount(), user, getRemoteIP(), form.getType(), null,
                    form.getMessage());
            storeLoggedUser(user);
            return redirectToSourceOr(Pages.userDetail(user.getAccountId()));
        });
    }

    private String prepareLogin(LoginForm form) {
        form.setType(AccessType.WEB);
        if (form.getUsername() == null) {
            form.setUsername(getCookieValue(COOKIE_USERNAME));
        }
        setPageTitle(getRequestParameter(PAGE_TITLE_NAME));
        return Pages.USER_LOGIN;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String initLogin(@ModelAttribute(MODEL_LOGIN_FORM) LoginForm form, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performIfLogged(user -> {
            return performText("注销当前账号后再登录");
        }, page -> {
            storeSourcePage();
            return prepareLogin(form);
        });
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String processLogin(@Valid @ModelAttribute(MODEL_LOGIN_FORM) LoginForm form, BindingResult br,
            HttpServletRequest req, HttpServletResponse res, Model m) {
        handleRequest(req, m);
        return performIfLogged(user -> {
            return performText("注销当前账号后再登录");
        }, page -> {
            // number of try count
            int tries = getSessionAttribute(MODEL_LOGIN_TRIES, 1);
            if (tries > MAX_LOGIN_TRIES) { // login try count limit
                m.addAttribute(MODEL_ERROR_TEXT, tr("user.login.error.outOfTries", MAX_LOGIN_TRIES));
                return prepareLogin(form);
            }

            if (!validateSubmit(form, br)) {
                return prepareLogin(form);
            }

            if (!doLogin(form, tries, res)) {
                setSessionAttribute(MODEL_LOGIN_TRIES, ++tries);
                return prepareLogin(form);
            }
            return redirectToSourceOr(Pages.userDetail(getLoggedUser().getAccountId()));
        });
    }

    private void saveAutoLoginCookie(HttpServletResponse res, String username, String token) {
        CookieUtils.setCookie(res, COOKIE_USERNAME, username, "/", MAX_COOKIE_AGE);
        CookieUtils.setCookie(res, COOKIE_TOKEN, token, "/", MAX_COOKIE_AGE);
    }

    private void deleteAutoLoginCookie(HttpServletResponse res) {
        CookieUtils.deleteCookie(res, COOKIE_USERNAME);
        CookieUtils.deleteCookie(res, COOKIE_TOKEN);
    }

    private boolean doLogin(LoginForm form, int tries, HttpServletResponse res) {
        final String username = form.getUsername();
        if (!userService.isAccountExist(username)) {
            model.addAttribute(MODEL_ERROR_TEXT, tr("user.login.error.validateFailed"));
            return false;
        }
        Account account = userService.getAccountByUsernameAndPassword(username, form.getPassword());
        if (account == null || !userService.loginAccount(account, getRemoteIP(), form.getType(), tries,
                request.getSession().getId(), form.getMessage())) {
            // username or password wrong
            model.addAttribute(MODEL_ERROR_TEXT, tr("user.login.error.validateFailed"));
            return false;
        }
        // remove tries from session
        setSessionAttribute(MODEL_LOGIN_TRIES, null);
        // add cookie if remember
        if (form.isRemember()) {
            // generate the key and save in DB and cookie
            saveAutoLoginCookie(res, username, userService.updateAutoLogin(username));
        } else {
            deleteAutoLoginCookie(res);
        }
        storeLoggedUser(userService.getUserById(account.getAccountId()));
        return true;
    }

    /**
     * Checks cookies and auto log in.
     * <p>
     * Called by the dispatcher servlet.
     */
    public void tryAutoLogin(HttpServletRequest req, HttpServletResponse res) {
        handleRequest(req, null);
        User user = getLoggedUser();
        if (user != null) { // already logged
            logger.debug("user {} already logged", user);
            return;
        }
        String username = getCookieValue(COOKIE_USERNAME), token = getCookieValue(COOKIE_TOKEN);
        logger.debug("fetch cookie username {} and token {}", username, token);
        if (isEmpty(username) || isEmpty(token)) {
            // delete invalid cookies
            logger.debug("no username and token found");
            deleteAutoLoginCookie(res);
            return;
        }
        user = userService.tryAutoLogin(username, token, getRemoteIP(), AccessType.WEB, request.getSession().getId(),
                "auto login for cookie");
        if (user == null) {
            // login failed, delete invalid cookies
            logger.debug("username or token code invalid");
            deleteAutoLoginCookie(res);
            return;
        }
        logger.debug("user {} login successfully", user);
        storeLoggedUser(user);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest req, HttpServletResponse res) {
        handleRequest(req, null);
        User user = getLoggedUser();
        if (user != null) {
            userService.logoutAccount(user.getAccountId(), AccessType.WEB, request.getSession().getId());
            storeLoggedUser(null);
        }
        deleteAutoLoginCookie(res);
        return redirectToSourceOr(Pages.HOME_URL);
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String initReset(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return Pages.USER_RESET;
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public String processReset(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        String username = req.getParameter("username");
        if (isEmpty(username)) {
            // invalid access
            return Pages.ACCESS_DENIED;
        }
        String password = req.getParameter("password"), confirm = req.getParameter("confirm");
        if (isEmpty(password) || isEmpty(confirm)) {
            m.addAttribute(MODEL_ERROR_TEXT, "密码与确认密码不能为空！");
            return Pages.USER_RESET;
        }
        if (password.length() < 6 || password.length() > 16 || confirm.length() < 6 || confirm.length() > 16) {
            m.addAttribute(MODEL_ERROR_TEXT, "密码长度为6到16位！");
            return Pages.USER_RESET;
        }
        if (!password.equals(confirm)) {
            m.addAttribute(MODEL_ERROR_TEXT, "密码与确认密码不同！");
            return Pages.USER_RESET;
        }
        if (!userService.updatePassword(username, password, getRemoteIP(), AccessType.WEB, null, null)) {
            m.addAttribute(MODEL_ERROR_TEXT, "无此用户！");
            return Pages.USER_RESET;
        }
        User user = getLoggedUser();
        if (user == null) {
            return redirectedPath(Pages.USER_LOGIN);
        } else {
            return redirectedPath(Pages.userDetail(user.getAccountId()));
        }
    }

    /**
     * Shows details of user with specified user account ID.
     *
     * @param accountId
     * @param req
     * @param m
     * @return
     */
    @RequestMapping(value = "/detail/{accountId}", method = RequestMethod.GET)
    public String detail(@PathVariable("accountId") String accountId, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return prepareValue(accountId, Long::parseLong, id -> {
            User user = userService.getUserById(id);
            if (user == null) {
                return performText("No such user with account ID: " + id);
            }
            model.addAttribute("user", helper.prepareUser(MiscUtils.toMap(user), getLoggedUser()));
            return Pages.USER_DETAIL;
        }, (id, e) -> Pages.ERROR_404);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performList(Pages.USER_LIST, this::users);
    }

    @RequestMapping(value = "/list/tag/{tagId}")
    public String listForTag(@PathVariable("tagId") String tagId, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return prepareValue(tagId, Long::parseLong,
                id -> performList(Pages.USER_LIST, (page, size) -> usersForTag(id, page, size)),
                (id, e) -> performText("tagId must be integer: " + tagId));
    }

    @RequestMapping(value = "/search")
    public String search(@RequestParam("q") String name, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performList(Pages.USER_LIST, (page, size) -> usersByFuzzyName(name, page, size));
    }

    private Pager users(int page, int size) {
        return helper.prepareUsers(userService.getAllUsers(page, size), getLoggedUser());
    }

    private Pager usersForTag(long tagId, int page, int size) {
        Tag tag = merchantService.getTagById(tagId);
        if (tag == null) {
            return new EmptyPager(size);
        }
        setPageTitle("标签\"{0}\"相关用户", tag.getName());
        return helper.prepareUsers(userService.getUsersForTag(tagId, page, size), getLoggedUser());
    }

    private Pager usersByFuzzyName(String name, int page, int size) {
        setPageTitle("搜索用户\"{0}\"的结果", name);
        return helper.prepareUsers(userService.getUsersByFuzzyName(name, page, size), getLoggedUser());
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String initEdit(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performIfLogged(user -> {
            return Pages.USER_DETAIL;
        }, page -> Pages.ACCESS_DENIED);
    }

    /**
     * Edits profile of current logged user.
     *
     * @param req
     * @param m
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String processEdit(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performIfLogged(user -> {
            return Pages.USER_EDIT;
        }, page -> Pages.ACCESS_DENIED);
    }

    /**
     * Edits settings of current logged user.
     *
     * @param req
     * @param m
     * @return
     */
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String editSettings(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performIfLogged(user -> {
            return Pages.USER_SETTINGS;
        }, page -> Pages.ACCESS_DENIED);
    }

    /**
     * Manages tags of current logged user.
     *
     * @param req
     * @param m
     * @return
     */
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public String editTags(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performIfLogged(user -> {
            model.addAttribute("tags", helper
                    .prepareTags(PropertyMapList.forObjects(merchantService.getTagsByVendor(user.getAccountId())), user)
                    .getContents());
            return Pages.USER_TAGS;
        }, page -> Pages.ACCESS_DENIED);
    }

    @RequestMapping(value = "/tags/append/{tagId}", method = RequestMethod.POST)
    @ResponseBody
    public Object appendTag(@PathVariable("tagId") String tagId, HttpServletRequest req) {
        handleRequest(req, null);
        SimpleResponseBuilder rb = new SimpleResponseBuilder();
        return performIfLogged(user -> {
            return prepareValue(tagId, Long::parseLong, id -> {
                List<Tag> tags = merchantService.getTagsByVendor(user.getAccountId());
                if (!MiscUtils.isEmpty(tags) && tags.parallelStream().anyMatch(tag -> tag.getTagId() == id)) {
                    return rb.failure("You have already serviced tag: " + tagId).toMap();
                }
                merchantService.serviceTag(user.getAccountId(), id,
                        getRequestParameter("location", Long::parseLong, 0L), getRequestParameter("intro"));
                return rb.success("You append tag: " + tagId).toMap();
            }, (id, e) -> rb.failure("the tag id is not valid integer: " + tagId).toMap());
        }, page -> rb.failure("You must login first").toMap());
    }

    @RequestMapping(value = "/tags/remove/{tagId}", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteTag(@PathVariable("tagId") String tagId, HttpServletRequest req) {
        handleRequest(req, null);
        SimpleResponseBuilder rb = new SimpleResponseBuilder();
        return performIfLogged(user -> {
            return prepareValue(tagId, Long::parseLong, id -> {
                List<Tag> tags = merchantService.getTagsByVendor(user.getAccountId());
                if (MiscUtils.isEmpty(tags)) {
                    return rb.failure("You have no tags").toMap();
                }
                if (!tags.parallelStream().anyMatch(tag -> tag.getTagId() == id)) {
                    return rb.failure("You have not serviced tag: " + tagId).toMap();
                }
                merchantService.discardTag(user.getAccountId(), id);
                return rb.success("You discard tag: " + tagId).toMap();
            }, (id, e) -> rb.failure("the tag id is not valid integer: " + tagId).toMap());
        }, page -> rb.failure("you must login first").toMap());
    }

    /**
     * Manages subscription of current logged user.
     *
     * @param req
     * @param m
     * @return
     */
    @RequestMapping(value = "/subscription", method = RequestMethod.GET)
    public String editSubscription(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performIfLogged(user -> {
            Subscription subscription = subscribeService.getSubscriptionOfUser(user.getAccountId());
            if (subscription == null) {
                return Pages.USER_SUBSCRIPTION;
            }
            List<Map<String, Object>> tags;
            if (!MiscUtils.isEmpty(subscription.getTags())) {
                tags = new LinkedList<>();
                subscription.getTags().forEach(item -> {
                    Tag tag = merchantService.getTagById(item.getId());
                    MapBuilder<String, Object> mb = MapBuilder.forObject(tag);
                    mb.put("time", item.getTime());
                    tags.add(helper.prepareTag(mb.toMap(), user));
                });
            } else {
                tags = Collections.emptyList();
            }
            List<Map<String, Object>> merchants;
            if (!MiscUtils.isEmpty(subscription.getMerchants())) {
                merchants = new LinkedList<>();
                subscription.getMerchants().forEach(item -> {
                    User merchant = userService.getUserById(item.getId());
                    MapBuilder<String, Object> mb = MapBuilder.forObject(merchant);
                    mb.put("time", item.getTime());
                    merchants.add(helper.prepareUser(mb.toMap(), user));
                });
            } else {
                merchants = Collections.emptyList();
            }
            model.addAttribute("tags", tags);
            model.addAttribute("merchants", merchants);
            return Pages.USER_SUBSCRIPTION;
        }, page -> Pages.ACCESS_DENIED);
    }

    @RequestMapping(value = "/subscribers/{accountId}")
    public String listSubscribers(@PathVariable("accountId") String accountId, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performList(Pages.USER_LIST, (page, size) -> getSubscribers(accountId, page, size));
    }

    private Pager getSubscribers(String accountId, int page, int size) {
        return prepareValue(accountId, Long::parseLong, id -> {
            User user = userService.getUserById(id);
            if (user == null) {
                return new EmptyPager(size);
            }
            User currentUser = getLoggedUser();
            setPageTitle("{0}的订阅用户", myNameOr(user, currentUser));
            model.addAttribute("for_merchant", 1);
            return helper.prepareSubscribers(subscribeService.getSubscriptionsForMerchant(id, page, size), currentUser);
        }, (id, e) -> new EmptyPager(size));
    }
}
