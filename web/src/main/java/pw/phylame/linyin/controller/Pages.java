/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.controller;

/**
 * @author Peng Wan
 */
public interface Pages {
    String HOME = "home/index";
    String ABOUT = "home/about";

    String RESULT_LIST = "home/results";

    String HOME_URL = "/";
    String ABOUT_URL = "/about";

    String MESSAGE_LIST = "lps/message/list";

    String MESSAGE_DETAIL = "lps/message/detail";
    String MESSAGE_DETAIL_URL = "/message/detail/";

    static <T> String messageDetail(T messageId) {
        return MESSAGE_DETAIL_URL + messageId;
    }

    String TAG_NEW = "lps/tag/new";

    String TAG_LIST = "lps/tag/list";

    String TAG_DETAIL = "lps/tag/detail";
    String TAG_DETAIL_URL = "/tag/detail/";

    static <T> String tagDetail(T tagId) {
        return TAG_DETAIL_URL + tagId;
    }

    String TAG_SEARCH_URL = "/tag/search";

    static String searchTag(String keyword) {
        return TAG_SEARCH_URL + "?q=" + keyword;
    }

    String PUBLISH = "lps/publish";

    String SUBSCRIBE = "lps/subscribe";

    String OCS_INDEX = "ocs/index";
    String OCS_API = "ocs/api";

    String APP_NEW = "ocs/app/new";
    String APP_LIST = "ocs/app/list";
    String APP_DETAIL = "ocs/app/detail";
    String APP_DETAIL_URL = "ocs/app/detail/";

    static <T> String appDetail(T appId) {
        return APP_DETAIL_URL + appId;
    }

    String USER_REGISTER = "user/register";
    String USER_LOGIN = "user/login";

    String USER_LIST = "user/list";

    String USER_DETAIL = "user/detail";
    String USER_DETAIL_URL = "user/detail/";

    static <T> String userDetail(T accountId) {
        return USER_DETAIL_URL + accountId;
    }

    String USER_SEARCH_URL = "/user/search";

    static String searchUser(String keyword) {
        return USER_SEARCH_URL + "?q=" + keyword;
    }

    String USER_RESET = "user/reset";
    String USER_EDIT = "user/edit";
    String USER_SETTINGS = "user/settings";

    String USER_TAGS = "user/tags";
    String USER_SUBSCRIPTION = "user/subscription";


    String ERROR_404 = "error/404";
    String PLAIN_PAGE = "error/plain";
    String ACCESS_DENIED = "error/accessDenied";
}
