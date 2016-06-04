/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.ui;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Peng Wan
 */
public class EmptyPager implements Pager {
    private int size;

    public EmptyPager(int size) {
        this.size = size;
    }

    @Override
    public int getTotalPages() {
        return 1;
    }

    @Override
    public long getTotalElements() {
        return 0;
    }

    @Override
    public int getCurrentPage() {
        return 1;
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public List<Map<String, Object>> getContents() {
        return Collections.emptyList();
    }

    @Override
    public boolean isFirst() {
        return true;
    }

    @Override
    public boolean isLast() {
        return true;
    }

}
