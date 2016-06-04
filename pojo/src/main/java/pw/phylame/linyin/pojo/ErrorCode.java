/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.pojo;

/**
 * @author Peng Wan
 */
public enum ErrorCode {
    NO_SUCH_ACCOUNT(-100020, "No such account registered in server"),

    NOT_LOGGED_IN(-100021, "You had not logged into system"),

    CLIENT_NOT_REGISTERED(-100029, ""),

    NO_JSON_DATA(-100030, "Your JSON data is empty , please set the HTTP body."),

    BAD_JSON_DATA(-100031, "Your HTTP body data does not meet the json format."),

    NOT_SUBSCRIBED(-100032, "You had not subscribed any content"),

    NO_MATCHED_MERCHANT(-100033, "No merchant nearby for you request"),

    NO_JSON_TYPE(-100034, "Not found Json-Type field in request header"),

    TMQ_EXPIRED(-100035, "Your requirement is expired"),

    NO_REQUIREMENT_TAGS(-100036, "You had not specified any requirement tag");

    private int code;
    private String message;

    /**
     * @param code    the digit of error code
     * @param message brief message of the error
     */
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
