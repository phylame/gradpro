/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service.chain.ocs;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import pw.phylame.linyin.constants.ErrorCode;
import pw.phylame.linyin.util.JsonUtils;
import pw.phylame.linyin.util.StringUtils;

public class FetchJsonCommand extends OcsCommand {
    @Override
    protected boolean execute() {
        String jsonData;
        try {
            jsonData = IOUtils.toString(context.getRequest().getInputStream());
        } catch (IOException e) {
            return abortChain(ErrorCode.NETWORK_ERROR, "cannot get data from HTTP request");
        }
        logger.debug("Get json data {}", jsonData);
        if (StringUtils.isEmpty(jsonData)) {
            return abortChain(ErrorCode.NO_JSON_DATA, "JSON data is empty");
        }
        if (!JsonUtils.validate(jsonData)) {
            return abortChain(ErrorCode.BAD_JSON_DATA, "Malformed JSON data");
        }
        context.setJsonData(jsonData);
        return CONTINUE_PROCESSING;
    }
}
