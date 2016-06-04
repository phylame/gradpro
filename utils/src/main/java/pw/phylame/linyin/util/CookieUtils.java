/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.CookieGenerator;

public class CookieUtils {
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public static void setCookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
        CookieGenerator cookie = new CookieGenerator();
        cookie.setCookieName(name);
        cookie.setCookiePath(path);
        cookie.setCookieMaxAge(maxAge);
        cookie.addCookie(response, value);
    }

    public static void deleteCookie(HttpServletResponse response, String name, String path) {
        CookieGenerator cookie = new CookieGenerator();
        cookie.setCookieName(name);
        cookie.setCookiePath(path);
        cookie.removeCookie(response);
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        CookieGenerator cookie = new CookieGenerator();
        cookie.setCookieName(name);
        cookie.removeCookie(response);
    }
}
