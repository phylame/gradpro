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
public class ValidateAccountCommand extends OcsCommand {
    @Autowired
    private UserService userService;

    @Override
    protected boolean execute() {
        long accountId = context.getAccountId();

        // not found this account
        if (!userService.isAccountExist(accountId)) {
            logger.debug("user {} not registered", accountId);
            return abortChain(ErrorCode.NO_SUCH_ACCOUNT, "user not registe: " + accountId);
        }

        // not login
        if (!userService.isAccountLoggedIn(accountId, AccessType.MOBILE, context.getClientId())) {
            logger.debug("user {} not logged", accountId);
            return abortChain(ErrorCode.NOT_LOGGED_IN, "user not logged: " + accountId);
        }
        return CONTINUE_PROCESSING;
    }
}
