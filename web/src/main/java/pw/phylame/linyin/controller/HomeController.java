/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pw.phylame.linyin.ui.EmptyPager;
import pw.phylame.linyin.util.StringUtils;

/**
 * @author Peng Wan
 */
@Controller
public class HomeController extends BaseController {
    private static final String PARAM_SEARCH_SCOPE = "scope";
    private static final String PARAM_SEARCH_KEYWORD = "q";

    @RequestMapping(value = {"/", "/index"})
    public String index(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return Pages.HOME;
    }

    @RequestMapping(value = "/search")
    public String search(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        String scope = getRequestParameter(PARAM_SEARCH_SCOPE, "all"), q = getRequestParameter(PARAM_SEARCH_KEYWORD);
        if (StringUtils.isBlank(q)) {
            return performText("no keyword inputted!");
        }
        switch (scope) {
            case "all":
                return performList(Pages.RESULT_LIST, (page, size) -> {
                    return new EmptyPager(size);
                });
            case "tag":
                return forwardedPath(Pages.TAG_SEARCH_URL);
            case "user":
                return forwardedPath(Pages.USER_SEARCH_URL);
            default:
                return performText("unknown scope: {0}", scope);
        }
    }

    @RequestMapping(value = "/about")
    public String about(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return Pages.ABOUT;
    }
}
