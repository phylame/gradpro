/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import pw.phylame.linyin.data.repository.SubscriptionRepository;
import pw.phylame.linyin.data.repository.TmrRepository;
import pw.phylame.linyin.domain.Subscription;
import pw.phylame.linyin.domain.Subscription.Item;
import pw.phylame.linyin.pojo.ocs.TmrData;
import pw.phylame.linyin.ui.Pager;
import pw.phylame.linyin.ui.SpringPageAdapter;
import pw.phylame.linyin.util.MiscUtils;

/**
 * @author Peng Wan
 */
@Service
public class SubscribeService extends CommonService {
    @Autowired
    private TmrRepository tmrRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    // true if success, false if already subscribed
    public boolean subscribeTag(long accountId, long tagId) {
        Subscription subscription = subscriptionRepository.findOne(accountId);
        if (subscription == null) {
            logger.debug("create new subscription for user {}", accountId);
            subscriptionRepository.insert(Subscription.forNewTag(accountId, tagId));
        } else {
            if (!subscription.newTagItem(tagId)) {
                return false;
            }
            subscriptionRepository.save(subscription);
        }
        return true;
    }

    public boolean unsubscribeTag(long accountId, long tagId) {
        Subscription subscription = subscriptionRepository.findOne(accountId);
        if (subscription == null || MiscUtils.isEmpty(subscription.getTags())) {
            return false;
        }
        for (Iterator<Item> iterator = subscription.getTags().iterator(); iterator.hasNext(); ) {
            Item item = iterator.next();
            if (item.getId() == tagId) {
                iterator.remove();
                subscriptionRepository.save(subscription);
                return true;
            }
        }
        return false;
    }

    public boolean subscribeMerchant(long accountId, long merchantId) {
        Subscription subscription = subscriptionRepository.findOne(accountId);
        if (subscription == null) {
            logger.debug("create new subscription for user {}", accountId);
            subscriptionRepository.insert(Subscription.forNewMerchant(accountId, merchantId));
        } else {
            if (!subscription.newMerchantItem(merchantId)) {
                return false;
            }
            subscriptionRepository.save(subscription);
        }
        return true;
    }

    public boolean unsubscribeMerchant(long accountId, long merchantId) {
        Subscription subscription = subscriptionRepository.findOne(accountId);
        if (subscription == null || MiscUtils.isEmpty(subscription.getMerchants())) {
            return false;
        }
        for (Iterator<Item> iterator = subscription.getMerchants().iterator(); iterator.hasNext(); ) {
            Item item = iterator.next();
            if (item.getId() == merchantId) {
                iterator.remove();
                subscriptionRepository.save(subscription);
                return true;
            }
        }
        return false;
    }

    public boolean isTagSubscribed(long accountId, long tagId) {
        // Subscription subscription =
        // subscriptionRepository.findOne(accountId);
        // if (subscription == null) {
        // return false;
        // }
        // if (subscription.getTags() == null) {
        // return false;
        // }
        // return subscription.getTags().stream().anyMatch(tag -> tag.getId() ==
        // tagId);
        return subscriptionRepository.isSubscribeTag(accountId, tagId) == 1;
    }

    public boolean isMerchantSubscribed(long accountId, long merchantId) {
        // Subscription subscription =
        // subscriptionRepository.findOne(accountId);
        // if (subscription == null) {
        // return false;
        // }
        // if (subscription.getMerchants() == null) {
        // return false;
        // }
        // return subscription.getMerchants().parallelStream().anyMatch(m ->
        // m.getId() == merchantId);
        return subscriptionRepository.isSubscribeMerchant(accountId, merchantId) == 1;
    }

    public Subscription getSubscriptionOfUser(long accountId) {
        return subscriptionRepository.findOne(accountId);
    }

    public Pager getAllSubscriptions(int page, int size) {
        return new SpringPageAdapter<>(subscriptionRepository.findAll(new PageRequest(page - 1, size)));
    }

    public Pager getSubscriptionsForTag(long tagId, int page, int size) {
        return new SpringPageAdapter<>(subscriptionRepository.findByTag(tagId, new PageRequest(page - 1, size)));
    }

    public Pager getSubscriptionsForMerchant(long merchantId, int page, int size) {
        return new SpringPageAdapter<>(
                subscriptionRepository.findByMerchant(merchantId, new PageRequest(page - 1, size)));
    }

    public Pager getSubscriptionsForTagAndMerchant(long tagId, long merchantId, int page, int size) {
        return new SpringPageAdapter<>(
                subscriptionRepository.findByTagAndMerchant(tagId, merchantId, new PageRequest(page - 1, size)));
    }

    public int subscriptionNumberForTag(long tagId) {
        return subscriptionRepository.countOfTag(tagId);
    }

    public int subscriptionNumberForMerchant(long merchantId) {
        return subscriptionRepository.countOfMerchant(merchantId);
    }

    public void saveRequirement(TmrData tmr) {
        tmrRepository.save(tmr);
    }

    public TmrData getRequirementOfUser(long accountId) {
        return tmrRepository.findOne(accountId);
    }
}
