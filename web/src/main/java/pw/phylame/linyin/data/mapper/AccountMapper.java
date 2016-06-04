/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.data.mapper;

import java.util.Map;

import pw.phylame.linyin.domain.Account;

public interface AccountMapper {
    Account selectById(Long accountId);

    Account selectByUsername(String username);

    Account selectByUsernameAndPassword(Map<String, String> param);

    int deleteById(Long accountId);

    int insert(Account account);

    int updateById(Account account);
}
