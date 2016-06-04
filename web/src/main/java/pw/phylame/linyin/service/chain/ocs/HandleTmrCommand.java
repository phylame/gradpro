/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service.chain.ocs;

import org.springframework.beans.factory.annotation.Autowired;

import pw.phylame.linyin.constants.ErrorCode;
import pw.phylame.linyin.pojo.ocs.TmrData;
import pw.phylame.linyin.service.SubscribeService;
import pw.phylame.linyin.util.JsonUtils;
import pw.phylame.linyin.util.MiscUtils;

/**
 * @author Peng Wan
 */
public class HandleTmrCommand extends OcsCommand {
    @Autowired
    private SubscribeService subscribeService;

    @Override
    protected boolean execute() {
        // as get json command executed, the result always be available
        TmrData tmr = JsonUtils.toObject(context.getJsonData(), TmrData.class);
        tmr.setAccountId(context.getAccountId());
        logger.debug("user {} send requirement tags {}", context.getAccountId(), tmr.getTags());

        // tags empty
        if (MiscUtils.isEmpty(tmr.getTags())) {
            logger.debug("user {} send requirement without tags", context.getAccountId());
            return abortChain(ErrorCode.NO_REQUIREMENT_TAGS, "tags is empty");
        }

        // requirement is not living
        if (!tmr.isLiving()) {
            logger.debug("user {} send requirement not living", context.getAccountId());
            return abortChain(ErrorCode.REQUIREMENT_NOT_LIVING, "requirement is not liveing");
        }

        // save requirement to db
        subscribeService.saveRequirement(tmr);
        logger.debug("received user {} requirement and save to db", context.getAccountId());
        context.getRespone().code(ErrorCode.OK);
        return PROCESSING_COMPLETE;
    }
}
