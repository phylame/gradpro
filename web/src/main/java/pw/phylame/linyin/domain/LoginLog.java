/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginLog extends CommonLog {
    private static final long serialVersionUID = -1242985977972337361L;

    private long accountId;
    private String type;
    private int tries;
    private String clientId;
}
