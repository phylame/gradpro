/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service.chain.ocs;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Peng Wan
 */
public abstract class OcsCommand implements Command {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected OcsContext context;

    @Override
    public final boolean execute(Context context) throws Exception {
        this.context = (OcsContext) context;
        return execute();
    }

    protected boolean abortChain(int code, Object data) {
        context.getRespone().code(code).data(data);
        return PROCESSING_COMPLETE;
    }

    protected abstract boolean execute();
}
