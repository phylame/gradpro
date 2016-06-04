/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pw.phylame.linyin.pojo.CachedPojo;

/**
 * Holds merchant service information.
 *
 * @author Peng Wan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Merchant extends CachedPojo {
    private static final long serialVersionUID = -4664392859304892313L;

    private long tagId;

    private long vendorId;

    private long locationId;

    /**
     * Additional message of this service.
     */
    private String intro;

    @Override
    public String getMemkey() {
        return super.getMemkey() + tagId + '.' + vendorId;
    }

}
