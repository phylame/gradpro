/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends CachedPojo {
    private static final long serialVersionUID = 2800830267211529597L;

    private long accountId;
    private String name;
    private String telephone;
    private long locationId;
    private String intro;

    @Override
    public String getMemkey() {
        return super.getMemkey() + accountId;
    }
}
