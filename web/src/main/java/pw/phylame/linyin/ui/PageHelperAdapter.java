/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.ui;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.pagehelper.Page;

import pw.phylame.linyin.util.MiscUtils;

/**
 * @author Peng Wan
 */
public class PageHelperAdapter<E> implements Pager {
    private Page<E> page;
    private List<Map<String, Object>> caches = null;

    public PageHelperAdapter(Page<E> page) {
        this.page = page;
    }

    public PageHelperAdapter(List<E> page) {
        this.page = (Page<E>) page;
    }

    @Override
    public int getTotalPages() {
        return page.getPages();
    }

    @Override
    public long getTotalElements() {
        return page.getTotal();
    }

    @Override
    public int getCurrentPage() {
        return page.getPageNum();
    }

    @Override
    public int getPageSize() {
        return page.getPageSize();
    }

    @Override
    public List<Map<String, Object>> getContents() {
        if (caches == null) {
            caches = page.getResult().stream().map(MiscUtils::toMap).collect(Collectors.toList());
        }
        return caches;
    }

    @Override
    public boolean isFirst() {
        return page.getPageNum() == 1;
    }

    @Override
    public boolean isLast() {
        return page.getPageNum() == page.getPages();
    }

}
