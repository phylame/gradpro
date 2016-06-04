/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service.chain.ocs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import pw.phylame.linyin.constants.ErrorCode;
import pw.phylame.linyin.pojo.Tag;
import pw.phylame.linyin.pojo.User;
import pw.phylame.linyin.pojo.ocs.TmlData;
import pw.phylame.linyin.pojo.ocs.TmrData;
import pw.phylame.linyin.service.MerchantService;
import pw.phylame.linyin.service.SubscribeService;
import pw.phylame.linyin.service.UserService;
import pw.phylame.linyin.util.JsonUtils;
import pw.phylame.linyin.util.MiscUtils;

/**
 * @author Peng Wan
 */
public class HandleTmlCommand extends OcsCommand {
    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private UserService userService;

    @Override
    protected boolean execute() {
        // as get json command executed, the result always be available
        TmlData tml = JsonUtils.toObject(context.getJsonData(), TmlData.class);
        logger.debug("user {} send location {}", context.getAccountId(), tml.getLocation());

        // get user requirement data
        TmrData tmr = subscribeService.getRequirementOfUser(context.getAccountId());
        if (tmr == null) {
            logger.debug("not found requirement for user {}", context.getAccountId());
            return abortChain(ErrorCode.USER_NOT_SUBSCRIBED, "not found tmr for account: " + context.getAccountId());
        }

        logger.debug("get tmr {} for user {}", tmr, context.getAccountId());

        if (!tmr.isLiving()) {
            logger.debug("found requirement but not living", tmr);
            return abortChain(ErrorCode.REQUIREMENT_NOT_LIVING, "requirement is not living");
        }

        List<Long> uTags = tmr.getTags(), mTags;
        logger.debug("tags of user {} is {}", context.getAccountId(), uTags);

        // get nearby merchant(s) account ID
        List<Long> merchants = merchantService.getNearbyMerchants(tml.getLocation());
        if (MiscUtils.isEmpty(merchants)) {
            return abortChain(ErrorCode.NO_NEARBY_MERCHANT, "no merchants nearby found");
        }

        logger.debug("found nearby {} merchant(s)", merchants.size());

        // holds account ID of merchants than can server the user
        List<User> results = new ArrayList<>();
        for (Long merchantId : merchants) {
            mTags = new LinkedList<>(merchantService.getTagsByVendor(merchantId).stream().map(Tag::getTagId)
                    .collect(Collectors.toList()));
            logger.debug("get merchant tags {} of {}", mTags, merchantId);
            mTags.retainAll(uTags);
            if (!mTags.isEmpty()) {
                results.add(userService.getUserById(merchantId));
            }
        }
        if (results.isEmpty()) {
            // no merchant server for you
            logger.debug("user {} request {} but no service", context.getAccountId(), uTags);
            return abortChain(ErrorCode.NO_MATCHED_MERCHANT, "no matched merchants for your requirement");
        }
        // now, send message
        logger.debug("found {} merchants can server for {}", results.size(), context.getAccountId());
        return abortChain(ErrorCode.OK, results);
    }

}
