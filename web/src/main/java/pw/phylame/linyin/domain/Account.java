/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.domain;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pw.phylame.linyin.pojo.CachedPojo;
import pw.phylame.linyin.pojo.Expireable;

@Data
@EqualsAndHashCode(callSuper = false)
public class Account extends CachedPojo implements Expireable {
    private static final long serialVersionUID = 2745331971777364298L;

    private long accountId;
    private String username;
    private String password;
    private String role;
    private Date expiration;

    public boolean isAdmin() {
        return "A".equals(role);
    }

    public boolean isUser() {
        return "U".equals(role);
    }

    @Override
    public String getMemkey() {
        return super.getMemkey() + accountId;
    }
}
