/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import pw.phylame.linyin.data.mapper.MerchantMapper;
import pw.phylame.linyin.data.mapper.TagMapper;
import pw.phylame.linyin.data.mapper.UserMapper;
import pw.phylame.linyin.domain.Merchant;
import pw.phylame.linyin.pojo.GeoLocation;
import pw.phylame.linyin.pojo.Tag;
import pw.phylame.linyin.pojo.User;
import pw.phylame.linyin.ui.PageHelperAdapter;
import pw.phylame.linyin.ui.Pager;
import pw.phylame.linyin.util.MiscUtils;

@Service
public class MerchantService extends CommonService {
    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    public long insertTag(String name, long creatorId, String intro) {
        Tag tag = tagMapper.selectByName(name);
        if (tag != null) {
            return -1;
        }
        tag = new Tag(0L, name, creatorId, intro);
        tagMapper.insert(tag);
        // also provide service for this tag
        serviceTag(creatorId, tag.getTagId(), 0L, intro);
        return tag.getTagId();
    }

    public long getTagNumber() {
        return tagMapper.count();
    }

    public List<Tag> getAllTags() {
        return tagMapper.selectAll();
    }

    public List<Tag> getAllTags(int limit) {
        return selectAll(tagMapper, limit);
    }

    public Pager getAllTags(int page, int size) {
        return selectAll(tagMapper, page, size);
    }

    public List<Tag> getTagsByCreator(long creatorId) {
        return tagMapper.selectByCreator(creatorId);
    }

    public Pager getTagsByCreator(long creatorId, int page, int size) {
        PageHelper.startPage(page, size);
        return new PageHelperAdapter<>(tagMapper.selectByCreator(creatorId));
    }

    public void serviceTag(long vendorId, long tagId, long locationId, String intro) {
        merchantMapper.insert(new Merchant(tagId, vendorId, 0L, intro));
    }

    public void discardTag(long vendorId, long tagId) {
        merchantMapper.deleteByTagAndVendor(MiscUtils.asMap("tagId", tagId, "vendorId", vendorId));
    }

    public List<Tag> getTagsByVendor(long vendorId) {
        return merchantMapper.selectTagsByVendor(vendorId);
    }

    public Pager getTagsByVendor(long vendorId, int page, int size) {
        PageHelper.startPage(page, size);
        return new PageHelperAdapter<>(merchantMapper.selectTagsByVendor(vendorId));
    }

    public Tag getTagById(long tagId) {
        return tagMapper.selectById(tagId);
    }

    public Tag getTagByIdAndVendor(long tagId, long vendorId) {
        return merchantMapper.selectTagByTagAndVendor(MiscUtils.asMap("tagId", tagId, "merchantId", vendorId));
    }

    public List<Tag> getTagsByFuzzyName(String name) {
        return tagMapper.selectByNameLike("%" + name + "%");
    }

    public Pager getTagsByFuzzyName(String name, int page, int size) {
        PageHelper.startPage(page, size);
        return new PageHelperAdapter<>(tagMapper.selectByNameLike("%" + name + "%"));
    }

    /**
     * Gets all merchants nearby for specified GPS location.
     *
     * @param location the base location
     * @return list of merchant account ID
     */
    public List<Long> getNearbyMerchants(GeoLocation location) {
        return getRandomMerchants(location);
    }

    private List<Long> getRandomMerchants(GeoLocation location) {
        return userMapper.selectAll().stream().filter(user -> user.getAccountId() != 0).map(User::getAccountId)
                .collect(Collectors.toList());
    }

}
