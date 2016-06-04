/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service.chain.ocs;

import org.springframework.beans.factory.annotation.Autowired;

import pw.phylame.linyin.constants.ErrorCode;
import pw.phylame.linyin.service.DeviceService;

/**
 * Validates the device with client ID of request.
 *
 * @author Peng Wan
 */
public class ValidateClientCommand extends OcsCommand {
    @Autowired
    private DeviceService deviceService;

    @Override
    public boolean execute() {
        String clientId = context.getClientId();
        if (!deviceService.isDeviceAvailable(clientId)) {
            logger.debug("client whit ID {} is not available", clientId);
            return abortChain(ErrorCode.CLIENT_NOT_AVAILABLE, "client is unavailable: " + clientId);
        }
        return CONTINUE_PROCESSING;
    }
}
