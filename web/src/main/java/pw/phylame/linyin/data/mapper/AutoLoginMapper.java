/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.data.mapper;

import pw.phylame.linyin.domain.AutoLogin;

public interface AutoLoginMapper {
    AutoLogin selectByUsername(String username);

    int insertOrUpdate(AutoLogin autoLogin);

    int deleteByUsername(String username);
}
