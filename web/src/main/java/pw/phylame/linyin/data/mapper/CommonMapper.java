/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */
package pw.phylame.linyin.data.mapper;

import java.util.List;

/**
 * @author Peng Wan
 */
public interface CommonMapper<ET, ID> {
    long count();

    List<ET> selectAll();

    ET selectById(ID id);

    int deleteById(ID id);

    int insert(ET entity);

    int updateById(ET entity);
}
