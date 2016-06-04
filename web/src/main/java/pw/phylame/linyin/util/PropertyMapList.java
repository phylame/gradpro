/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */
package pw.phylame.linyin.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Peng Wan
 */
public class PropertyMapList implements Iterable<Map<String, Object>> {
    private List<Map<String, Object>> caches;

    public <T> PropertyMapList(List<T> list) {
        caches = list.stream().map(MiscUtils::toMap).collect(Collectors.toList());
    }

    @Override
    public Iterator<Map<String, Object>> iterator() {
        return getContents().iterator();
    }

    public List<Map<String, Object>> getContents() {
        return caches;
    }

    public static <T> PropertyMapList forObjects(List<T> list) {
        return new PropertyMapList(list);
    }
}
