/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Object that supports be cached in memcached.
 *
 * @author Peng Wan
 */
public interface Cacheable extends Serializable {

    /**
     * Key of the POJO storing in memcached.
     * <p>
     * This implementation just contains POJO full class name and a dot
     * character, and subclass can just add its id or key.
     *
     * @return the string represent this POJO
     */
    @JsonIgnore
    String getMemkey();
}
