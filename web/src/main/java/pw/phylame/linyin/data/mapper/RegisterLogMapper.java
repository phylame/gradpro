/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.data.mapper;

import java.util.List;

import pw.phylame.linyin.domain.RegisterLog;

public interface RegisterLogMapper {
    List<RegisterLog> selectAll();

    RegisterLog selectById(Long logId);

    int deleteById(Long logId);

    int insert(RegisterLog log);

    int updateById(RegisterLog log);
}
