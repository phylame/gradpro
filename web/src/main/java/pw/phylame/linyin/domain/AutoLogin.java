/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.domain;

import java.util.Date;

import lombok.Data;
import pw.phylame.linyin.pojo.Expireable;

@Data
public class AutoLogin implements Expireable {
    private String username;

    private String token;

    private Date expiration;
}
