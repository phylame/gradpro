/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.chain.Chain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pw.phylame.linyin.constants.ErrorCode;
import pw.phylame.linyin.constants.HttpConstants;
import pw.phylame.linyin.service.MerchantService;
import pw.phylame.linyin.service.chain.ocs.OcsContext;
import pw.phylame.linyin.util.PropertyMapList;
import pw.phylame.linyin.util.SimpleResponseBuilder;

/**
 * OCS - The Open Client Service controller.
 * <p>
 * Handles HTTP request from mobile client, just POST and PUT received.
 * </p>
 *
 * @author Peng Wan
 */
@Controller
@RequestMapping(value = "/ocs")
public class OcsController extends BaseController {
    private static final String JSON_TYPE_HEADER = HttpConstants.CONTENT_TYPE + '=' + HttpConstants.MIME_JSON_TEXT;

    @Autowired
    @Qualifier("loginChain")
    private Chain loginChain;

    @Autowired
    @Qualifier("logoutChain")
    private Chain logoutChain;

    @Autowired
    @Qualifier("tmrChain")
    private Chain tmrChain;

    @Autowired
    @Qualifier("tmlChain")
    private Chain tmlChain;

    @Autowired
    @Qualifier("getsubChain")
    private Chain getsubChain;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private ControllerHelper helper;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return Pages.OCS_INDEX;
    }

    @RequestMapping(value = "/api")
    public String api() {
        return Pages.OCS_API;
    }

    @RequestMapping(value = "/user/register/{clientId}", headers = {JSON_TYPE_HEADER}, method = {RequestMethod.POST,
            RequestMethod.PUT})
    @ResponseBody
    public Object registerUser(@PathVariable("clientId") String clientId, HttpServletRequest req) {
        return performChain(null, clientId, null, req);
    }

    @RequestMapping(value = "/user/login/{clientId}", headers = {JSON_TYPE_HEADER}, method = {RequestMethod.POST,
            RequestMethod.PUT})
    @ResponseBody
    public Object userLogin(@PathVariable("clientId") String clientId, HttpServletRequest req) {
        return performChain(loginChain, clientId, null, req);
    }

    @RequestMapping(value = "/user/logout/{clientId}/{accountId}", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public Object userLogout(@PathVariable("clientId") String clientId, @PathVariable("accountId") String accountId,
            HttpServletRequest req) {
        return performChain(logoutChain, clientId, accountId, req);
    }

    @RequestMapping(value = "/user/detail/{clientId}/{accountId}", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public Object userDetail(@PathVariable("clientId") String clientId, @PathVariable("accountId") String accountId,
            HttpServletRequest req) {
        return performChain(null, clientId, accountId, req);
    }

    @RequestMapping(value = "/user/update/{clientId}/{accountId}", headers = {JSON_TYPE_HEADER}, method = {
            RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public Object updateUser(@PathVariable("clientId") String clientId, @PathVariable("accountId") String accountId,
            HttpServletRequest req) {
        return performChain(null, clientId, accountId, req);
    }

    @RequestMapping(value = "/user/subscriptions/{clientId}/{accountId}", method = {RequestMethod.POST,
            RequestMethod.PUT})
    @ResponseBody
    public Object userSubscriptions(@PathVariable("clientId") String clientId,
            @PathVariable("accountId") String accountId, HttpServletRequest req) {
        return performChain(getsubChain, clientId, accountId, req);
    }

    @RequestMapping(value = "/subscribe/tag/{clientId}/{accountId}", headers = {JSON_TYPE_HEADER}, method = {
            RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public Object subscribeTags(@PathVariable("clientId") String clientId, @PathVariable("accountId") String accountId,
            HttpServletRequest req) {
        return performChain(null, clientId, accountId, req);
    }

    @RequestMapping(value = "/unsubscribe/tag/{clientId}/{accountId}", headers = {JSON_TYPE_HEADER}, method = {
            RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public Object unsubscribeTags(@PathVariable("clientId") String clientId,
            @PathVariable("accountId") String accountId, HttpServletRequest req) {
        return performChain(null, clientId, accountId, req);
    }

    @RequestMapping(value = "/subscribe/user/{clientId}/{accountId}", headers = {JSON_TYPE_HEADER}, method = {
            RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public Object subscribeUsers(@PathVariable("clientId") String clientId, @PathVariable("accountId") String accountId,
            HttpServletRequest req) {
        return performChain(null, clientId, accountId, req);
    }

    @RequestMapping(value = "/unsubscribe/user/{clientId}/{accountId}", headers = {JSON_TYPE_HEADER}, method = {
            RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public Object unsubscribeUsers(@PathVariable("clientId") String clientId,
            @PathVariable("accountId") String accountId, HttpServletRequest req) {
        return performChain(null, clientId, accountId, req);
    }

    @RequestMapping(value = "/tag/all", method = RequestMethod.POST)
    @ResponseBody
    public Object getAllTags(HttpServletRequest req) {
        handleRequest(req, null);
        return SimpleResponseBuilder.simple(true,
                helper.prepareTags(new PropertyMapList(merchantService.getAllTags()), getLoggedUser()).getContents())
                .toMap();
    }

    @RequestMapping(value = "/tmr/new/{clientId}/{accountId}", headers = {JSON_TYPE_HEADER}, method = {
            RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public Object newTmr(@PathVariable("clientId") String clientId, @PathVariable("accountId") String accountId,
            HttpServletRequest req) {
        return performChain(tmrChain, clientId, accountId, req);
    }

    @RequestMapping(value = "/tmr/update/{clientId}/{accountId}", headers = {JSON_TYPE_HEADER}, method = {
            RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public Object updateTmr(@PathVariable("clientId") String clientId, @PathVariable("accountId") String accountId,
            HttpServletRequest req) {
        return performChain(null, clientId, accountId, req);
    }

    @RequestMapping(value = "/tmr/discard/{clientId}/{accountId}", headers = {JSON_TYPE_HEADER}, method = {
            RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public Object discardTmr(@PathVariable("clientId") String clientId, @PathVariable("accountId") String accountId,
            HttpServletRequest req) {
        return performChain(null, clientId, accountId, req);
    }

    @RequestMapping(value = "/tml/{clientId}/{accountId}", headers = {JSON_TYPE_HEADER}, method = {RequestMethod.POST,
            RequestMethod.PUT})
    @ResponseBody
    public Object newTml(@PathVariable("clientId") String clientId, @PathVariable("accountId") String accountId,
            HttpServletRequest req) {
        return performChain(tmlChain, clientId, accountId, req);
    }

    private Object performChain(Chain chain, String clientId, String accountId, HttpServletRequest request) {
        OcsContext context = new OcsContext();
        context.setRequest(request);
        context.setClientId(clientId);
        if (accountId != null) { // require account ID
            try {
                context.setAccountId(Long.parseLong(accountId));
            } catch (NumberFormatException e) {
                return SimpleResponseBuilder
                        .simple(ErrorCode.BAD_ACCOUNT_ID, "\"accountId\" must be valid integer, but: " + accountId)
                        .toMap();
            }
        }
        context.setRespone(new SimpleResponseBuilder());
        return executeChain(chain, context);
    }

    private Object executeChain(Chain chain, OcsContext context) {
        try {
            chain.execute(context);
        } catch (Exception e) {
            logger.debug("unexpected exception occurs", e);
            return SimpleResponseBuilder.simple(ErrorCode.SERVER_ERROR, "Server internal error").toMap();
        }
        return context.getRespone().toMap();
    }
}
