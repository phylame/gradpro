/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service;

import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.PageHelper;

import pw.phylame.linyin.constants.Constants;
import pw.phylame.linyin.data.mapper.CommonMapper;
import pw.phylame.linyin.ui.PageHelperAdapter;
import pw.phylame.linyin.ui.Pager;

/**
 * @author Peng Wan
 */
public class CommonService implements Constants {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected <ET, ID> List<ET> selectAll(CommonMapper<ET, ID> mapper, int limit) {
        return limitQuery(mapper::selectAll, limit);
    }

    protected <ET, ID> Pager selectAll(CommonMapper<ET, ID> mapper, int page, int size) {
        return pagingQuery(mapper::selectAll, page, size);
    }

    protected <E> List<E> limitQuery(Supplier<List<E>> query, int limit) {
        if (limit > 0) {
            PageHelper.startPage(1, limit);
        }
        return query.get();
    }

    protected <E> Pager pagingQuery(Supplier<List<E>> query, int page, int size) {
        PageHelper.startPage(page, size);
        return new PageHelperAdapter<>(query.get());
    }
}
