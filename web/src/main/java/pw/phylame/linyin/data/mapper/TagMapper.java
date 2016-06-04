/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.data.mapper;

import java.util.List;

import pw.phylame.linyin.pojo.Tag;

public interface TagMapper extends CommonMapper<Tag, Long> {
    Tag selectByName(String name);

    List<Tag> selectByCreator(long creatorId);

    List<Tag> selectByNameLike(String name);
}
