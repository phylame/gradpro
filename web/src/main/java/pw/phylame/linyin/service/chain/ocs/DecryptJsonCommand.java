/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */
package pw.phylame.linyin.service.chain.ocs;

/**
 * @author Peng Wan
 */
public class DecryptJsonCommand extends OcsCommand {
    @Override
    protected boolean execute() {
        // now, do nothing
        return CONTINUE_PROCESSING;
    }

}
