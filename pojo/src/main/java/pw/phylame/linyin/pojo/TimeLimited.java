/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pw.phylame.linyin.util.DateUtils;

/**
 * Object that contains start time and end time.
 *
 * @author Peng Wan
 */
public interface TimeLimited {

    Date getStartTime();

    Date getEndTime();

    /**
     * Tests this pojo is started or not.
     *
     * @return <code>true</code> if started, otherwise <code>false</code>
     */
    @JsonIgnore
    default boolean isStarted() {
        return DateUtils.beforeNow(getStartTime());
    }

    /**
     * Tests this pojo is living or not.
     *
     * @return <tt>true</tt> if living otherwise <tt>not</tt>
     */
    @JsonIgnore
    default boolean isLiving() {
        return DateUtils.beforeNow(getStartTime()) && DateUtils.afterNow(getEndTime());
    }

    /**
     * Check this pojo is stopped or not.
     *
     * @return <code>true</code> if stopped, otherwise <code>false</code>
     */
    @JsonIgnore
    default boolean isStopped() {
        return DateUtils.beforeNow(getEndTime());
    }
}
