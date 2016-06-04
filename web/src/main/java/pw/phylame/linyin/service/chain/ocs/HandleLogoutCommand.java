/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */
package pw.phylame.linyin.service.chain.ocs;

import org.springframework.beans.factory.annotation.Autowired;

import pw.phylame.linyin.constants.AccessType;
import pw.phylame.linyin.constants.ErrorCode;
import pw.phylame.linyin.service.UserService;

/**
 * @author Peng Wan
 */
public class HandleLogoutCommand extends OcsCommand {
    @Autowired
    private UserService userService;

    @Override
    protected boolean execute() {
        userService.logoutAccount(context.getAccountId(), AccessType.MOBILE, context.getClientId());
        context.getRespone().code(ErrorCode.OK);
        return PROCESSING_COMPLETE;
    }
}
