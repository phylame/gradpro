/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pw.phylame.linyin.pojo.CachedPojo;
import pw.phylame.linyin.pojo.Expireable;

/**
 * Data holder for storing login status.
 *
 * @author Peng Wan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "login")
public class LoginState extends CachedPojo {
    private static final long serialVersionUID = 3110649034488720454L;

    @Id
    private long accountId;

    private List<Item> records;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item implements Expireable {
        /**
         * Login type, see {@link pw.phylame.linyin.constants.AccessType} for
         * more information.
         */
        private String type;

        /**
         * ID of the client device.
         * <p>
         * For Web login set this value with session ID.
         */
        private String client;

        /**
         * Expired time.
         */
        private Date expiration;
    }
}
