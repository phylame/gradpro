/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */
package pw.phylame.linyin.service.chain.ocs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import pw.phylame.linyin.constants.ErrorCode;
import pw.phylame.linyin.domain.Subscription;
import pw.phylame.linyin.service.SubscribeService;
import pw.phylame.linyin.util.SimpleResponseBuilder;

/**
 * @author Peng Wan
 */
public class GetSubscriptionCommand extends OcsCommand {
    @Autowired
    private SubscribeService subscribeService;

    @Override
    protected boolean execute() {
        SimpleResponseBuilder respone = context.getRespone();
        Subscription subscription = subscribeService.getSubscriptionOfUser(context.getAccountId());
        List<String> topics = new ArrayList<>(16);
        if (subscription != null) {
            if (subscription.getTags() != null) {
                subscription.getTags().forEach(item -> topics.add("lps/+/" + item.getId()));
            }
            if (subscription.getMerchants() != null) {
                subscription.getMerchants().forEach(item -> topics.add("lps/" + item.getId() + "/+"));
            }
        }
        respone.code(ErrorCode.OK).data(topics);
        return PROCESSING_COMPLETE;
    }

}
