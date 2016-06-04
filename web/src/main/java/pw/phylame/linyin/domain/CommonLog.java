/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.domain;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pw.phylame.linyin.pojo.CachedPojo;

/**
 * @author Peng Wan
 */
@Data
@EqualsAndHashCode(callSuper = true)
abstract class CommonLog extends CachedPojo {
    private static final long serialVersionUID = -6737604721430761958L;

    private long logId;
    private Date time;
    private String ip;
    private String message;

    @Override
    public String getMemkey() {
        return super.getMemkey() + logId;
    }
}
