/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */
package pw.phylame.linyin.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import pw.phylame.linyin.pojo.Message;

/**
 * @author Peng Wan
 */
public interface MessageRepository extends MongoRepository<Message, String> {
    int countByTag(long tagId);

    List<Message> findByTag(long tagId);

    Page<Message> findByTag(long tagId, Pageable pageable);

    int countByVendor(long vendorId);

    List<Message> findByVendor(long accountId);

    Page<Message> findByVendor(long accountId, Pageable pageable);

    List<Message> findByTagAndVendor(long tagId, long vendorId);

    Page<Message> findByTagAndVendor(long tagId, long vendorId, Pageable pageable);
}
