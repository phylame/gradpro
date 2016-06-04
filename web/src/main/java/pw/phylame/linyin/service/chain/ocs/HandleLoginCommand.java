/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service.chain.ocs;

import org.springframework.beans.factory.annotation.Autowired;

import pw.phylame.linyin.constants.AccessType;
import pw.phylame.linyin.constants.ErrorCode;
import pw.phylame.linyin.domain.Account;
import pw.phylame.linyin.pojo.ocs.LoginData;
import pw.phylame.linyin.service.UserService;
import pw.phylame.linyin.util.HttpUtils;
import pw.phylame.linyin.util.JsonUtils;
import pw.phylame.linyin.util.SimpleResponseBuilder;

/**
 * @author Peng Wan
 */
public class HandleLoginCommand extends OcsCommand {
    @Autowired
    private UserService userService;

    @Override
    protected boolean execute() {
        LoginData data = JsonUtils.toObject(context.getJsonData(), LoginData.class);
        Account account = userService.getAccountByUsernameAndPassword(data.getUsername(), data.getPassword());
        SimpleResponseBuilder respone = context.getRespone().code(ErrorCode.OK);
        if (account == null) {
            logger.debug("login with username {} and password {} failed", data.getUsername(), data.getPassword());
            respone.code(ErrorCode.NO_SUCH_ACCOUNT).data("username or password is wrong");
        } else if (userService.isAccountLoggedIn(account.getAccountId(), AccessType.MOBILE, context.getClientId())) {
            logger.debug("user {} already logged and not expired", data.getUsername());
            respone.data(account.getAccountId());
        } else if (account.isExpired()) {
            logger.debug("user account is expired {}", data.getUsername());
            respone.data(ErrorCode.ACCOUNT_EXPIRED).data("account is expired");
        } else {
            userService.loginAccount(account.getAccountId(), HttpUtils.getRemoteIP(context.getRequest()),
                    AccessType.MOBILE, 1, context.getClientId(), "login by device");
            respone.data(account.getAccountId());
            logger.debug("user {} login success", data.getUsername());
        }
        return PROCESSING_COMPLETE;
    }

}
