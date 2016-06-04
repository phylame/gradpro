/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service.chain.ocs;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.chain.impl.ContextBase;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pw.phylame.linyin.util.SimpleResponseBuilder;

/**
 * Context for OCS request.
 *
 * @author Peng Wan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OcsContext extends ContextBase {
    private static final long serialVersionUID = -2393168137020385884L;

    private String clientId;
    private long accountId;
    private String jsonData;
    private HttpServletRequest request;

    /**
     * Builder for response result of the chain.
     */
    private SimpleResponseBuilder respone;
}
