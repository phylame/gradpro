/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.data.mapper;

import java.util.List;

import pw.phylame.linyin.domain.LoginLog;

public interface LoginLogMapper {
    List<LoginLog> selectAll();

    LoginLog selectById(Long logId);

    int deleteById(Long logId);

    int insert(LoginLog log);

    int updateById(LoginLog log);
}
