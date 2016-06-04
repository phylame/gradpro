/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.data.mapper;

import java.util.List;

import pw.phylame.linyin.domain.UserLog;

public interface UserLogMapper {
    List<UserLog> selectAll();

    UserLog selectById(Long logId);

    int deleteById(Long logId);

    int insert(UserLog log);

    int updateById(UserLog log);
}
