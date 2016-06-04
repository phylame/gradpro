/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.controller;

import static pw.phylame.linyin.util.MiscUtils.toMap;

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
import org.springframework.web.bind.annotation.RequestParam;

import pw.phylame.linyin.constants.AccessType;
import pw.phylame.linyin.controller.form.TagForm;
import pw.phylame.linyin.pojo.Tag;
import pw.phylame.linyin.pojo.User;
import pw.phylame.linyin.service.MerchantService;
import pw.phylame.linyin.service.SubscribeService;
import pw.phylame.linyin.service.UserService;
import pw.phylame.linyin.ui.EmptyPager;
import pw.phylame.linyin.ui.Pager;

@Controller
@RequestMapping("/tag")
public class TagController extends BaseController {
    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private UserService userService;

    @Autowired
    private ControllerHelper helper;

    private String forNew(TagForm form, User user) {
        form.setType(AccessType.WEB);
        return Pages.TAG_NEW;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initNew(@ModelAttribute("tagForm") TagForm form, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performIfLogged(user -> forNew(form, user), page -> Pages.ACCESS_DENIED);
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String processNew(@Valid @ModelAttribute("tagForm") TagForm form, BindingResult br, HttpServletRequest req,
            Model m) {
        handleRequest(req, m);
        return performIfLogged(user -> {
            if (!validateSubmit(form, br)) {
                return forNew(form, user);
            }
            long tagId = merchantService.insertTag(form.getName(), user.getAccountId(), form.getIntro());
            return redirectedPath(Pages.tagDetail(tagId));
        }, page -> Pages.ACCESS_DENIED);
    }

    @RequestMapping(value = "/list")
    public String list(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performList(Pages.TAG_LIST, this::tags);
    }

    @RequestMapping(value = "/list/creator/{creatorId}")
    public String listForCreator(@PathVariable("creatorId") String creatorId, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performList(Pages.TAG_LIST, (page, size) -> tagsForCreator(creatorId, page, size));
    }

    @RequestMapping(value = "/search")
    public String search(@RequestParam("q") String name, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        setPageTitle("搜索标签\"{0}\"的结果", name);
        return performList(Pages.TAG_LIST, (page, size) -> tagsByFuzzyName(name, page, size));
    }

    private Pager tags(int page, int size) {
        setPageTitle("全部标签");
        return helper.prepareTags(merchantService.getAllTags(page, size), getLoggedUser());
    }

    private Pager tagsByFuzzyName(String name, int page, int size) {
        return helper.prepareTags(merchantService.getTagsByFuzzyName(name, page, size), getLoggedUser());
    }

    private Pager tagsForCreator(String creatorId, int page, int size) {
        return prepareValue(creatorId, Long::parseLong, id -> {
            User user = userService.getUserById(id);
            if (user == null) {
                return new EmptyPager(size);
            }
            User currentUser = getLoggedUser();
            setPageTitle("{0}创建的标签", myNameOr(user, currentUser));
            model.addAttribute("for_creator", 1);
            return helper.prepareTags(merchantService.getTagsByCreator(id, page, size), currentUser);
        }, (id, e) -> new EmptyPager(size));
    }

    @RequestMapping(value = "/detail/{tagId}")
    public String detail(@PathVariable("tagId") String tagId, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return prepareValue(tagId, Long::parseLong, id -> {
            Tag tag = merchantService.getTagById(id);
            if (tag == null) {
                return performText("no such tag with ID " + id);
            }
            model.addAttribute("tag", helper.prepareTag(toMap(tag), getLoggedUser()));
            return Pages.TAG_DETAIL;
        }, (id, e) -> Pages.ERROR_404);
    }

    @RequestMapping(value = "/edit/{tagId}", method = RequestMethod.GET)
    public String edit(@PathVariable("tagId") String tagId, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return prepareValue(tagId, Long::parseLong, id -> performText("under development"), (id, e) -> Pages.ERROR_404);
    }

    @RequestMapping(value = "/subscribers/{tagId}")
    public String listSubscribers(@PathVariable("tagId") String tagId, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performList(Pages.USER_LIST, (page, size) -> getSubscribers(tagId, page, size));
    }

    private Pager getSubscribers(String tagId, int page, int size) {
        return prepareValue(tagId, Long::parseLong, id -> {
            Tag tag = merchantService.getTagById(id);
            if (tag == null) {
                return new EmptyPager(size);
            }
            setPageTitle("\"{0}\"的订阅用户", tag.getName());
            model.addAttribute("for_tag", 1);
            return helper.prepareSubscribers(subscribeService.getSubscriptionsForTag(id, page, size), getLoggedUser());
        }, (id, e) -> new EmptyPager(size));
    }
}
