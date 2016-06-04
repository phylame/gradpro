/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.data.mapper;

import java.util.List;

import pw.phylame.linyin.pojo.User;

public interface UserMapper extends CommonMapper<User, Long> {

    List<User> selectByNameLike(String name);

    List<User> selectByTagId(long tagId);

    long countForTagId(long tagId);

}
