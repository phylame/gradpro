/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pw.phylame.linyin.pojo.CachedPojo;
import pw.phylame.linyin.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Peng Wan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "subscription")
public class Subscription extends CachedPojo {
    private static final long serialVersionUID = 2866285608028075690L;

    /**
     * The account ID of user.
     */
    @Id
    private long accountId;

    /**
     * List of subscribed tag ID.
     */
    private List<Item> tags;

    /**
     * List of subscribed merchant ID.
     */
    private List<Item> merchants;

    // true if success, false if already subscribed
    public boolean newTagItem(long tagId) {
        if (tags == null) {
            tags = new ArrayList<>();
        } else if (tags.stream().anyMatch(item -> item.id == tagId)) {
            return false;
        }
        tags.add(new Item(tagId, DateUtils.now()));
        return true;
    }

    // true if success, false if already subscribed
    public boolean newMerchantItem(long merchantId) {
        if (merchants == null) {
            merchants = new ArrayList<>();
        } else if (merchants.stream().anyMatch(item -> item.id == merchantId)) {
            return false;
        }
        merchants.add(new Item(merchantId, DateUtils.now()));
        return true;
    }

    public static Subscription forNewTag(long accountId, long tagId) {
        Subscription subscription = new Subscription();
        subscription.accountId = accountId;
        subscription.newTagItem(tagId);
        return subscription;
    }

    public static Subscription forNewMerchant(long accountId, long merchantId) {
        Subscription subscription = new Subscription();
        subscription.accountId = accountId;
        subscription.newMerchantItem(merchantId);
        return subscription;
    }

    @Data
    @AllArgsConstructor
    public static class Item {
        private long id;
        private Date time;
    }
}
