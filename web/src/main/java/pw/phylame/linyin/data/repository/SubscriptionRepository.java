/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pw.phylame.linyin.domain.Subscription;

/**
 * @author Peng Wan
 */
public interface SubscriptionRepository extends MongoRepository<Subscription, Long> {
    String TAG_PATTERN = "{tags:{ $all:[{$elemMatch:{_id: ?0}}]}}";
    String MERCHANT_PATTERN = "{merchants:{ $all:[{$elemMatch:{_id: ?0}}]}}";
    String TAG_MERCHANT_PATTERN = "{tags:{ $all:[{$elemMatch:{_id: ?0}}]},merchants:{ $all:[{$elemMatch:{_id: ?1}}]}}";

    String USER_TAG_PATTERN = "{_id:?0,tags:{ $all:[{$elemMatch:{_id: ?1}}]}}";
    String USER_MERCHANT_PATTERN = "{_id:?0,merchants:{ $all:[{$elemMatch:{_id: ?1}}]}}";

    @Query(value = TAG_PATTERN, count = true)
    int countOfTag(long tagId);

    @Query(value = MERCHANT_PATTERN, count = true)
    int countOfMerchant(long merchantId);

    @Query(value = TAG_PATTERN)
    Page<Subscription> findByTag(long tagId, Pageable pageable);

    @Query(value = MERCHANT_PATTERN)
    Page<Subscription> findByMerchant(long merchantId, Pageable pageable);

    @Query(value = TAG_MERCHANT_PATTERN)
    Page<Subscription> findByTagAndMerchant(long tagId, long merchantId, Pageable pageable);

    @Query(value = USER_TAG_PATTERN, count = true)
    int isSubscribeTag(long accountId, long tagId);

    @Query(value = USER_MERCHANT_PATTERN, count = true)
    int isSubscribeMerchant(long accountId, long merchantId);
}
