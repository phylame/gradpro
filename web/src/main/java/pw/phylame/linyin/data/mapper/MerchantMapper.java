/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.data.mapper;

import java.util.List;
import java.util.Map;

import pw.phylame.linyin.domain.Merchant;
import pw.phylame.linyin.pojo.Tag;

public interface MerchantMapper {
    List<Tag> selectTagsByTag(Long tagId);

    List<Tag> selectTagsByVendor(Long accountId);

    // keys: tagId, vendorId
    Tag selectTagByTagAndVendor(Map<String, String> param);

    // keys: tagId, vendorId
    Merchant selectByTagAndVendor(Map<String, String> params);

    int deleteByTagAndVendor(Map<String, String> params);

    int insert(Merchant merchant);

    int update(Merchant merchant);
}
