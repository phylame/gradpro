/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */
package pw.phylame.linyin.controller;

import static pw.phylame.linyin.util.MiscUtils.toMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pw.phylame.linyin.pojo.Message;
import pw.phylame.linyin.pojo.Tag;
import pw.phylame.linyin.pojo.User;
import pw.phylame.linyin.service.MerchantService;
import pw.phylame.linyin.service.MessageService;
import pw.phylame.linyin.service.UserService;
import pw.phylame.linyin.ui.EmptyPager;
import pw.phylame.linyin.ui.Pager;
import pw.phylame.linyin.util.SimpleResponseBuilder;

/**
 * @author Peng Wan
 */
@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {
    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ControllerHelper helper;

    @RequestMapping(value = "/list")
    public String list(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performList(Pages.MESSAGE_LIST, this::messages);
    }

    @RequestMapping(value = "/list/tag/{tagId}")
    public String listForTag(@PathVariable("tagId") String tagId, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performList(Pages.MESSAGE_LIST, (page, size) -> messagesForTag(tagId, page, size));
    }

    @RequestMapping(value = "/list/merchant/{merchantId}")
    public String listForMerchant(@PathVariable("merchantId") String merchantId, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performList(Pages.MESSAGE_LIST, (page, size) -> messagesForMerchant(merchantId, page, size));
    }

    @RequestMapping(value = "/list/tag/{tagId}/merchant/{merchantId}")
    public String listForTagAndMerchant(@PathVariable("tagId") String tagId,
            @PathVariable("merchantId") String merchantId, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performList(Pages.MESSAGE_LIST,
                (page, size) -> messagesForTagAndMerchant(tagId, merchantId, page, size));
    }

    private Pager messages(int page, int size) {
        setPageTitle("所有消息");
        return helper.prepareMessages(messageService.getAllMessages(page, size), getLoggedUser());
    }

    private Pager messagesForTag(String tagId, int page, int size) {
        return prepareValue(tagId, Long::parseLong, id -> {
            Tag tag = merchantService.getTagById(id);
            if (tag == null) {
                return new EmptyPager(size);
            }
            setPageTitle("\"{0}\"相关消息", tag.getName());
            model.addAttribute("for_tag", 1);
            return helper.prepareMessages(messageService.getMessagesByTag(id, page, size), getLoggedUser());
        }, (id, e) -> new EmptyPager(size));
    }

    private Pager messagesForMerchant(String vendorId, int page, int size) {
        return prepareValue(vendorId, Long::parseLong, id -> {
            User user = userService.getUserById(id);
            if (user == null) {
                return new EmptyPager(size);
            }
            User currentUser = getLoggedUser();
            setPageTitle("{0}的消息", myNameOr(user, currentUser));
            model.addAttribute("for_vendor", 1);
            return helper.prepareMessages(messageService.getMessagesByVendor(id, page, size), currentUser);
        }, (id, e) -> new EmptyPager(size));
    }

    private Pager messagesForTagAndMerchant(String tagId, String vendorId, int page, int size) {
        try {
            long _tagId = Long.parseLong(tagId), _vendorId = Long.parseLong(vendorId);
            Tag tag = merchantService.getTagById(_tagId);
            User user = userService.getUserById(_vendorId);
            if (tag == null || user == null) {
                return new EmptyPager(size);
            }
            User currentUser = getLoggedUser();
            setPageTitle("{0}的\"{1}\"相关消息", myNameOr(user, currentUser), tag.getName());
            model.addAttribute("for_all", 1);
            return helper.prepareMessages(messageService.getMessagesByTagAndVendor(_tagId, _vendorId, page, size),
                    currentUser);
        } catch (NumberFormatException e) {
            return new EmptyPager(size);
        }
    }

    @RequestMapping(value = "/detail/{messageId}")
    public String detail(@PathVariable("messageId") String messageId, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            return performText("no such message with ID " + messageId);
        }
        model.addAttribute("message", helper.prepareMessage(toMap(message)));
        return Pages.MESSAGE_DETAIL;
    }

    @RequestMapping(value = "/delete/{messageId}", method = RequestMethod.POST)
    @ResponseBody
    public Object delete(@PathVariable("messageId") String messageId, HttpServletRequest req) {
        handleRequest(req, null);
        SimpleResponseBuilder rb = new SimpleResponseBuilder();
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            return rb.state(false).data("未找到消息：" + messageId).toMap();
        }
        User user = getLoggedUser();
        if (user == null || user.getAccountId() != message.getVendor()) {
            return rb.state(false).data("无权删除此消息").toMap();
        }
        messageService.deleteMessage(messageId);
        return rb.state(true).toMap();
    }
}
