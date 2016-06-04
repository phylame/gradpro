/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Peng Wan
 */
public class MiscUtils {
    private static Logger logger = LoggerFactory.getLogger(MiscUtils.class);

    public static <E> boolean isEmpty(Collection<E> c) {
        return c == null || c.isEmpty();
    }

    public static <K, V> boolean isEmpty(Map<K, V> m) {
        return m == null || m.isEmpty();
    }

    public static <K, V> Map<K, V> asMap(Object... objects) {
        Map<K, V> map = new HashMap<>();
        fillMap(map, objects);
        return map;
    }

    public static <T> T find(Iterable<T> iterable, Predicate<T> predicate) {
        for (T t : iterable) {
            if (predicate.test(t)) {
                return t;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> void fillMap(Map<K, V> map, Object... objects) {
        if (objects == null || objects.length == 0) {
            return;
        }
        if (objects.length % 2 != 0) {
            throw new IllegalArgumentException("length of objects must % 2 = 0");
        }

        for (int i = 0; i < objects.length; i += 2) {
            map.put((K) objects[i], (V) objects[i + 1]);
        }
    }

    public static <V> Map<Integer, V> toMap(Collection<V> c) {
        Map<Integer, V> map = new HashMap<>();
        fillMap(map, c);
        return map;
    }

    public static <V> void fillMap(Map<Integer, V> map, Collection<V> c) {
        c.forEach(o -> map.put(map.size(), o));
    }

    public static Map<String, Object> toMap(Object object) {
        Map<String, Object> map = new HashMap<>();
        fillMap(map, object);
        return map;
    }

    public static class MapBuilder<K, V> {
        private Map<K, V> map;

        public MapBuilder(Map<K, V> map) {
            this.map = map;
        }

        public static <K, V> MapBuilder<K, V> newMap() {
            return new MapBuilder<>(new HashMap<>());
        }

        public static MapBuilder<String, Object> forObject(Object o) {
            return new MapBuilder<>(MiscUtils.toMap(o));
        }

        public Map<K, V> toMap() {
            return map;
        }

        public MapBuilder<K, V> put(K key, V value) {
            map.put(key, value);
            return this;
        }

        public MapBuilder<K, V> update(Map<K, V> m) {
            map.putAll(m);
            return this;
        }
    }

    public static void fillMap(Map<String, Object> map, Object object) {
        String name = null;
        try {
            // methods and inherited methods
            for (Method method : object.getClass().getMethods()) {
                // only process public and non-static method
                int modifiers = method.getModifiers();
                if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)) {
                    continue;
                }
                // no arguments
                if (method.getParameterCount() != 0) {
                    continue;
                }
                name = method.getName();
                if (isNormalGetter(name)) { // getXXX
                    name = name.substring(3);
                } else if (isBooleanGetter(name)) { // isXXX
                    name = name.substring(2);
                } else { // not getter
                    continue;
                }
                // to camel style
                map.put(toCamel(name), method.invoke(object));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger.debug("cannot invoke getter method " + name, e);
        }
    }

    private static boolean isNormalGetter(String name) {
        return name.startsWith("get");
    }

    private static boolean isBooleanGetter(String name) {
        return name.startsWith("is");
    }

    private static String toCamel(String text) {
        return Character.toLowerCase(text.charAt(0)) + text.substring(1);
    }
}
