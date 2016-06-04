/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.ui;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Peng Wan
 */
public interface Pager extends Iterable<Map<String, Object>> {
    /**
     * Returns number of total pages.
     *
     * @return number of total pages
     */
    int getTotalPages();

    /**
     * Returns number of total elements.
     *
     * @return number of total elements.
     */
    long getTotalElements();

    /**
     * Returns the number of current page.
     *
     * @return the number of current page
     */
    int getCurrentPage();

    /**
     * Returns size of page.
     *
     * @return size of page
     */
    int getPageSize();

    /**
     * Returns list of elements (properties map) in current page.
     *
     * @return list of elements (properties map) in current page
     */
    List<Map<String, Object>> getContents();

    @Override
    default Iterator<Map<String, Object>> iterator() {
        return getContents().iterator();
    }

    /**
     * Returns weather current page is the first one.
     *
     * @return
     */
    boolean isFirst();

    /**
     * Returns weather current page is the last one.
     *
     * @return
     */
    boolean isLast();

}
