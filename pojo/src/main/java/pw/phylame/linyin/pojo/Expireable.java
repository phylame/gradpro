/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pw.phylame.linyin.util.DateUtils;

/**
 * Object that contains expired time.
 *
 * @author Peng Wan
 */
public interface Expireable {
    Date getExpiration();

    @JsonIgnore
    default boolean isExpired() {
        return getExpiration() != null && DateUtils.beforeNow(getExpiration());
    }
}
