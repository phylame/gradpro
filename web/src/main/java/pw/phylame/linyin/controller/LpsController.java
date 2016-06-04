/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.controller;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.function.BiFunction;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pw.phylame.linyin.constants.AccessType;
import pw.phylame.linyin.controller.form.PublishForm;
import pw.phylame.linyin.pojo.Message;
import pw.phylame.linyin.pojo.Tag;
import pw.phylame.linyin.pojo.User;
import pw.phylame.linyin.service.MerchantService;
import pw.phylame.linyin.service.MessageService;
import pw.phylame.linyin.service.SubscribeService;
import pw.phylame.linyin.service.UserService;
import pw.phylame.linyin.ui.EmptyPager;
import pw.phylame.linyin.ui.Pager;
import pw.phylame.linyin.util.DateUtils;
import pw.phylame.linyin.util.PropertyMapList;
import pw.phylame.linyin.util.SimpleResponseBuilder;

/**
 * LPS (Linyin publish and subscribe) controller.
 *
 * @author Peng Wan
 */
@Controller
public class LpsController extends BaseController {
    private static final String MODEL_PUBLISH_FORM = "pubForm";

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ControllerHelper helper;

    private String preparePublish(PublishForm form, User user) {
        form.setType(AccessType.WEB);
        if (form.getStartTime() == null) {
            form.setStartTime(DateUtils.calculateDateByNow('h', 1));
        }
        if (form.getEndTime() == null) {
            form.setEndTime(DateUtils.calculateDate(form.getStartTime(), 'd', 2));
        }
        long accountId = user.getAccountId();
        LinkedHashMap<String, String> tags = new LinkedHashMap<>();
        merchantService.getTagsByVendor(accountId).forEach(t -> tags.put(Long.toString(t.getTagId()), t.getName()));
        model.addAttribute("tags", tags);
        return Pages.PUBLISH;
    }

    @RequestMapping(value = "/publish", method = RequestMethod.GET)
    public String initPublish(@ModelAttribute(MODEL_PUBLISH_FORM) PublishForm form, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performOrLogin(user -> preparePublish(form, user));
    }

    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public String processPublish(@Valid @ModelAttribute(MODEL_PUBLISH_FORM) PublishForm form, BindingResult br,
            HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performOrLogin(user -> {
            if (!validateSubmit(form, br)) {
                return preparePublish(form, user);
            }
            if (!form.checkEndTime()) {
                addFieldError(br, MODEL_PUBLISH_FORM, "endTime", "pub.error.endTimeNotAfterStartTime");
                return preparePublish(form, user);
            }
            Message message = form.dumpToMessage();
            message.setVendor(user.getAccountId());
            if (!messageService.publishMessage(message)) {
                return preparePublish(form, user);
            }
            return redirectedPath(Pages.messageDetail(message.getMessageId()));
        });
    }

    @RequestMapping(value = "/subscribe")
    public String subscribeHome(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        int tagLimit = getRequestParameter("tagLimit", Integer::parseInt, 5),
                merchantLimit = getRequestParameter("merchantLimit", Integer::parseInt, 5);
        User currentUser = getLoggedUser();
        m.addAttribute("tagLimit", tagLimit);
        m.addAttribute("merchantLimit", merchantLimit);
        m.addAttribute("tagNumber", merchantService.getTagNumber());
        m.addAttribute("tags",
                helper.prepareTags(PropertyMapList.forObjects(merchantService.getAllTags(tagLimit)), currentUser)
                .getContents());
        m.addAttribute("merchantNumber", userService.getUserNumber());
        m.addAttribute("merchants",
                helper.prepareUsers(PropertyMapList.forObjects(userService.getAllUsers(merchantLimit)), currentUser)
                .getContents());
        return Pages.SUBSCRIBE;
    }

    private static final SimpleResponseBuilder FOR_NOT_LOGGED = SimpleResponseBuilder.simple(false, "需要先登录");

    @RequestMapping(value = "/tag/subscribe/{tagId}", method = RequestMethod.POST)
    @ResponseBody
    public Object subscribeTag(@PathVariable("tagId") String tagId, HttpServletRequest req) {
        handleRequest(req, null);
        return performSubscribe(tagId, subscribeService::subscribeTag, "请勿重复订阅标签：{0}");
    }

    @RequestMapping(value = "/tag/unsubscribe/{tagId}", method = RequestMethod.POST)
    @ResponseBody
    public Object unsubscribeTag(@PathVariable("tagId") String tagId, HttpServletRequest req) {
        handleRequest(req, null);
        return performSubscribe(tagId, subscribeService::unsubscribeTag, "标签未订阅：{0}");
    }

    @RequestMapping(value = "/user/subscribe/{accountId}", method = RequestMethod.POST)
    @ResponseBody
    public Object subscribeMerchant(@PathVariable("accountId") String accountId, HttpServletRequest req) {
        handleRequest(req, null);
        return performSubscribe(accountId, subscribeService::subscribeMerchant, "请勿重复订阅商家：{0}");
    }

    @RequestMapping(value = "/user/unsubscribe/{accountId}", method = RequestMethod.POST)
    @ResponseBody
    public Object unsubscribeMerchant(@PathVariable("accountId") String accountId, HttpServletRequest req) {
        handleRequest(req, null);
        return performSubscribe(accountId, subscribeService::unsubscribeMerchant, "商家未订阅：{0}");
    }

    private Object performSubscribe(String entityId, BiFunction<Long, Long, Boolean> task, String failureText) {
        return performIfLogged(user -> {
            return prepareValue(entityId, Long::parseLong, id -> {
                SimpleResponseBuilder rb = new SimpleResponseBuilder();
                if (!task.apply(user.getAccountId(), id)) {
                    rb.state(false).data(MessageFormat.format(failureText, id));
                } else {
                    rb.state(true);
                }
                return rb.toMap();
            }, (id, e) -> SimpleResponseBuilder.forError(e).toMap());
        }, page -> FOR_NOT_LOGGED.toMap());
    }

    @RequestMapping(value = "/subscribers/list")
    public String listAllSubscribers(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performList(Pages.USER_LIST, this::allSubscribers);
    }

    private Pager allSubscribers(int page, int size) {
        setPageTitle("所有订阅者");
        return helper.prepareSubscribers(subscribeService.getAllSubscriptions(page, size), getLoggedUser());
    }

    @RequestMapping(value = "/subscribers/tag/{tagId}/merchant/{merchantId}")
    public String listSubscribersForTagAndMerchant(@PathVariable("tagId") String tagId,
            @PathVariable("merchantId") String merchantId, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performList(Pages.USER_LIST,
                (page, size) -> subscribersForTagAndMerchant(tagId, merchantId, page, size));
    }

    private Pager subscribersForTagAndMerchant(String tagId, String merchantId, int page, int size) {
        try {
            long _tagId = Long.parseLong(tagId), _merchantId = Long.parseLong(merchantId);
            Tag tag = merchantService.getTagById(_tagId);
            User user = userService.getUserById(_merchantId);
            if (tag == null || user == null) {
                return new EmptyPager(size);
            }
            User currentUser = getLoggedUser();
            setPageTitle("{0}的\"{1}\"的订阅用户", myNameOr(user, currentUser), tag.getName());
            model.addAttribute("for_all", 1);
            return helper.prepareSubscribers(
                    subscribeService.getSubscriptionsForTagAndMerchant(_tagId, _merchantId, page, size), currentUser);
        } catch (NumberFormatException e) {
            return new EmptyPager(size);
        }
    }
}
