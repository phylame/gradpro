/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.pojo;

/**
 * Abstract implementation for {@link Cacheable}.
 *
 * @author Peng Wan
 */
public abstract class CachedPojo implements Cacheable {
    private static final long serialVersionUID = -4177822488838043316L;

    @Override
    public String getMemkey() {
        return getClass().getName() + '.';
    }
}
