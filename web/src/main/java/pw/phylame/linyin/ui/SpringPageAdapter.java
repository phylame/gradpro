/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.ui;

import org.springframework.data.domain.Page;
import pw.phylame.linyin.util.MiscUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpringPageAdapter<E> implements Pager {
    private Page<E> page;
    private List<Map<String, Object>> caches = null;

    public SpringPageAdapter(Page<E> page) {
        this.page = page;
    }

    @Override
    public int getTotalPages() {
        return page.getTotalPages();
    }

    @Override
    public long getTotalElements() {
        return page.getTotalElements();
    }

    @Override
    public int getCurrentPage() {
        return page.getNumber() + 1;
    }

    @Override
    public int getPageSize() {
        return page.getSize();
    }

    @Override
    public List<Map<String, Object>> getContents() {
        if (caches == null) {
            caches = page.getContent().stream().map(MiscUtils::toMap).collect(Collectors.toList());
        }
        return caches;
    }

    @Override
    public boolean isFirst() {
        return page.isFirst();
    }

    @Override
    public boolean isLast() {
        return page.isLast();
    }

}
