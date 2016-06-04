/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.controller;

import static pw.phylame.linyin.util.StringUtils.isEmpty;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.util.WebUtils;

import pw.phylame.linyin.controller.form.AccessCheckable;
import pw.phylame.linyin.pojo.User;
import pw.phylame.linyin.ui.Pager;
import pw.phylame.linyin.util.CookieUtils;
import pw.phylame.linyin.util.HttpUtils;

/**
 * @author Peng Wan
 */
class BaseController {
    static final String SESSION_CURRENT_USER = "currentUser";

    private static final String SESSION_SOURCE_URL = "from";

    static final String PAGE_TITLE_NAME = "pageTitle";

    private static final String MODEL_TEXT_CONTENT = "text";

    static final String MODEL_ERROR_TEXT = "errorText";

    private static final String MODEL_CURRENT_URL = "currentURL";

    private static final String MODEL_PAGER_OBJECT = "pager";

    private static final String MODEL_PAGE_INDICES = "pageIndices";

    private static final String PARAM_CURRENT_PAGE = "pageNumber";

    private static final String PARAM_PAGE_SIZE = "pageSize";

    static final String COOKIE_USERNAME = "username";

    static final String COOKIE_TOKEN = "token";

    private static final int DEFAULT_PAGE_SIZE = 8;

    private static final int PAGE_SHOWN_TAGS = 7;

    // 7 days
    static final int MAX_COOKIE_AGE = 604800;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    Model model;
    HttpServletRequest request = null;
    private RequestContext context = null;

    /**
     * Does preparation for current HTTP request.
     *
     * @param request
     *            the HTTP request
     */
    final void handleRequest(HttpServletRequest request, Model model) {
        this.model = model;
        this.request = request;
        context = new RequestContext(request);
        request.setAttribute(MODEL_CURRENT_URL, request.getRequestURL());
    }

    /**
     * Gets localized message for specified code.
     *
     * @param code
     *            the code for message
     * @return the message
     */
    final String tr(String code) {
        return context.getMessage(code);
    }

    /**
     * Gets localized message for specified code with format arguments.
     *
     * @param code
     *            the code for message
     * @param args
     *            arguments for message
     * @return the message
     */
    final String tr(String code, Object... args) {
        return context.getMessage(code, args);
    }

    /**
     * Gets the string represent the IP of HTTP request.
     *
     * @return the IP string
     */
    final String getRemoteIP() {
        return HttpUtils.getRemoteIP(request);
    }

    /**
     * Gets a request parameter value for specified name.
     *
     * @param name
     *            name of the parameter
     * @return the value
     */
    final String getRequestParameter(String name) {
        return request.getParameter(name);
    }

    final String getRequestParameter(String name, String defaultValue) {
        String value = request.getParameter(name);
        return value != null ? value : defaultValue;
    }

    final <T> T getRequestParameter(String name, Function<String, T> converter, T defaultValue) {
        String value = request.getParameter(name);
        if (isEmpty(value)) {
            return defaultValue;
        }
        try {
            return converter.apply(value);
        } catch (RuntimeException e) {
            logger.debug("cannot convert " + name + " with " + value, e);
            return defaultValue;
        }
    }

    /**
     * Gets a attribute value from current HTTP session.
     *
     * @param name
     *            name of the attribute
     * @param defaultValue
     *            default value to return if the <tt>name</tt> not present
     * @return the attribute value
     */
    final <T> T getSessionAttribute(String name, T defaultValue) {
        return HttpUtils.getSessionAttribute(request.getSession(), name, defaultValue);
    }

    final void setSessionAttribute(String name, Object value) {
        WebUtils.setSessionAttribute(request, name, value);
    }

    final String getCookieValue(String name) {
        return CookieUtils.getCookieValue(request, name);
    }

    /**
     * Checks access type and bind errors before processing the request. After
     * this method, the <code>context</code> will be available.
     *
     * @param form
     *            the form
     * @param bResult
     *            validation result
     * @return <tt>true</tt> if no errors, otherwise <tt>false</tt> if in bad
     *         access or validate error
     */
    final boolean validateSubmit(AccessCheckable form, BindingResult bResult) {
        if (bResult.hasErrors()) {
            return false;
        }
        if (!form.isValidAccess()) {
            model.addAttribute(MODEL_ERROR_TEXT, tr("error.invalidAccess"));
            return false;
        }
        return true;
    }

    final void addFieldError(BindingResult bResult, String objectName, String field, String code) {
        bResult.addError(new FieldError(objectName, field, tr(code)));
    }

    final void addFieldError(BindingResult bResult, String objectName, String field, String code, Object... args) {
        bResult.addError(new FieldError(objectName, field, tr(code, args)));
    }

    final void setPageTitle(String title) {
        model.addAttribute(PAGE_TITLE_NAME, title);
    }

    final void setPageTitle(String title, Object... args) {
        setPageTitle(MessageFormat.format(title, args));
    }

    final User getLoggedUser() {
        return getSessionAttribute(SESSION_CURRENT_USER, null);
    }

    final String myNameOr(User user) {
        return myNameOr(user, getLoggedUser());
    }

    final String myNameOr(User user, User currentUser) {
        return currentUser == null || currentUser.getAccountId() != user.getAccountId() ? '"' + user.getName() + '"'
                : "我";
    }

    final void storeSourcePage() {
        storeSourcePage(getRequestParameter(SESSION_SOURCE_URL));
    }

    final void storeSourcePage(String page) {
        setSessionAttribute(SESSION_SOURCE_URL, page);
    }

    final String redirectToSource() {
        return redirectToSourceOr(Pages.HOME_URL);
    }

    final String redirectToSourceOr(String target) {
        String page = getSessionAttribute(SESSION_SOURCE_URL, null);
        storeSourcePage(null);
        return redirectedPath(!isEmpty(page) ? page : target);
    }

    /**
     * Redirects to specified target path and append source URL.
     *
     * @param target
     *            target path
     * @return name for view resolver
     */
    final String redirectTo(String target) {
        return redirectedPath(target + HttpUtils.makeQuery(SESSION_SOURCE_URL, request.getRequestURL().toString()));
    }

    final String redirectedPath(String path) {
        return "redirect:" + formatPath(path);
    }

    final String forwardedPath(String path) {
        return "forward:" + formatPath(path);
    }

    private String formatPath(String path) {
        if (path.contains("://")) { // maybe contains schema
            return path;
        } else {
            return path.charAt(0) == '/' ? path : '/' + path;
        }
    }

    // success: user -> R, failure: loginPage -> R
    final <R> R performIfLogged(Function<User, R> success, Function<String, R> failure) {
        User user = getLoggedUser();
        if (user == null) {
            logger.debug("no user logged, go to login page");
            return failure.apply(redirectTo(Pages.USER_LOGIN));
        } else {
            return success.apply(user);
        }
    }

    // do specified task or go to login page
    final String performOrLogin(Function<User, String> task) {
        return performIfLogged(task, page -> {
            setPageTitle(tr("page.error.noLogin"));
            return page;
        });
    }

    final String performText(String text) {
        model.addAttribute(MODEL_TEXT_CONTENT, text);
        return Pages.PLAIN_PAGE;
    }

    final String performText(String text, Object... args) {
        return performText(MessageFormat.format(text, args));
    }

    /**
     * Renders results in listing view.
     *
     * @param viewName
     *            name of view for rendering model
     * @param provider
     *            provides pager object with current page index and page size
     * @return the view name
     */
    final <T> String performList(String viewName, BiFunction<Integer, Integer, Pager> provider) {
        int currentPage, pageSize;
        try {
            currentPage = Integer.parseInt(getRequestParameter(PARAM_CURRENT_PAGE, "1"));
            if (currentPage < 1) {
                currentPage = 1;
            }
            pageSize = Integer.parseInt(getRequestParameter(PARAM_PAGE_SIZE, "0"));
            if (pageSize < 1) {
                pageSize = DEFAULT_PAGE_SIZE;
            }
        } catch (NumberFormatException e) {
            currentPage = 1;
            pageSize = DEFAULT_PAGE_SIZE;
        }
        preparePager(provider.apply(currentPage, pageSize), currentPage, PAGE_SHOWN_TAGS);
        return viewName;
    }

    /**
     * Prepares for paging results.
     *
     * @param pager
     *            the pager object
     * @param currentPage
     *            index of current page(begin from 1)
     * @param pageLimits
     *            pageLimits of page indices shown in page bar
     */
    private <T> void preparePager(Pager pager, int currentPage, int pageLimits) {
        int size = pager.getTotalPages(), end = currentPage + pageLimits - 1, start = currentPage;
        if (end > size) { // no more pages
            start += size - end;
            end = size;
            if (start < 1) {
                start = 1;
            }
        }
        int pageIndices[] = new int[end - start + 1], beg = start;
        Arrays.setAll(pageIndices, i -> beg + i);
        model.addAttribute(MODEL_PAGER_OBJECT, pager);
        model.addAttribute(MODEL_PAGE_INDICES, pageIndices);
    }

    final <T, R> R prepareValue(String origin, Function<String, T> converter, Function<T, R> success,
            BiFunction<String, RuntimeException, R> error) {
        T v;
        try {
            v = converter.apply(origin);
        } catch (RuntimeException e) {
            return error.apply(origin, e);
        }
        return success.apply(v);
    }
}
