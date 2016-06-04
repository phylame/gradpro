/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;

import pw.phylame.linyin.controller.UserController;
import pw.phylame.linyin.data.repository.LoginStateRepository;

/**
 * Auto set encoding for request and response.
 *
 * @author Peng Wan
 */
public class LywebDispatcherServlet extends DispatcherServlet {
    private static final long serialVersionUID = 5763669416433412037L;

    private String encoding;

    private boolean forceEncoding = false;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        doExtraInit();
    }

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        internalFilter(request, response);
        super.doService(request, response);
    }

    private void doExtraInit() {
        encoding = getServletConfig().getInitParameter("encoding");
        forceEncoding = Boolean.parseBoolean(getServletConfig().getInitParameter("forceEncoding"));
        cleanupLoginStatus(getServletContext());
    }

    private ApplicationContext getSpringContent(ServletContext servletContext) {
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        if (context == null) {
            logger.error("not found Spring WebApplicationContext, let this listen behind ContextLoaderListener");
        }
        return context;
    }

    private void cleanupLoginStatus(ServletContext servletContext) {
        ApplicationContext context = getSpringContent(servletContext);
        LoginStateRepository repository = context.getBean(LoginStateRepository.class);
        repository.deleteAll();
        logger.debug("all previous login state cleaned");
    }

    private void internalFilter(HttpServletRequest request, HttpServletResponse response) throws Exception {
        setHttpEncoding(request, response);
        checkAutoLogin(request, response);
    }

    private void setHttpEncoding(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        if (encoding != null && (forceEncoding || request.getCharacterEncoding() == null)) {
            request.setCharacterEncoding(encoding);
            if (forceEncoding) {
                response.setCharacterEncoding(encoding);
            }
        }
    }

    private UserController userController = null;

    private void checkAutoLogin(HttpServletRequest request, HttpServletResponse response) {
        if (request.getSession().getAttribute("checked") != null) {
            logger.debug("already check auto login for current session");
            return;
        }
        logger.debug("check auto login...");
        request.getSession().setAttribute("checked", Boolean.TRUE);
        if (userController == null) {
            ApplicationContext context = getSpringContent(request.getServletContext());
            userController = context.getBean(UserController.class);
        }
        if (userController == null) {
            throw new RuntimeException("Not found UserController in application context");
        }
        userController.tryAutoLogin(request, response);
    }
}
