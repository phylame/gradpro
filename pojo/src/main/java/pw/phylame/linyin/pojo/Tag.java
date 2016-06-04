/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * An abstract merchandise tag.
 *
 * @author Peng Wan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tag extends CachedPojo {
    private static final long serialVersionUID = 5678875751975859334L;

    private long tagId;
    private String name;
    private long creator;
    private String intro;

    @Override
    public String getMemkey() {
        return super.getMemkey() + tagId;
    }
}
