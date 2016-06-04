/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.pojo.ocs;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import pw.phylame.linyin.pojo.CachedPojo;
import pw.phylame.linyin.pojo.GeoLocation;

/**
 * TML - Tell my location.
 *
 * @author Peng Wan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "tml")
public class TmlData extends CachedPojo {
    private static final long serialVersionUID = -6288081790760725360L;

    @Id
    private Long id;

    private GeoLocation location;

    @Override
    public String getMemkey() {
        return super.getMemkey() + id;
    }
}
