/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */
package pw.phylame.linyin.util;

import java.util.HashMap;

import pw.phylame.linyin.util.MiscUtils.MapBuilder;

/**
 * @author Peng Wan
 */
public class SimpleResponseBuilder extends MapBuilder<String, Object> {
    public SimpleResponseBuilder() {
        super(new HashMap<>());
    }

    public SimpleResponseBuilder state(boolean state) {
        put("state", state);
        return this;
    }

    public SimpleResponseBuilder code(int code) {
        put("code", code);
        return this;
    }

    public SimpleResponseBuilder data(Object data) {
        put("data", data);
        return this;
    }

    public SimpleResponseBuilder success(Object data) {
        return state(true).data(data);
    }

    public SimpleResponseBuilder failure(Object data) {
        return state(false).data(data);
    }

    public static SimpleResponseBuilder simple(boolean state, Object data) {
        return new SimpleResponseBuilder().state(state).data(data);
    }

    public static SimpleResponseBuilder simple(int code, Object data) {
        return new SimpleResponseBuilder().code(code).data(data);
    }

    public static SimpleResponseBuilder forError(Throwable t) {
        return new SimpleResponseBuilder().state(false).data(t.getLocalizedMessage());
    }
}
