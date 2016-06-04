/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.controller.form;

import pw.phylame.linyin.constants.AccessType;

/**
 * Object that contains access type information.
 *
 * @author Peng Wan
 */
public interface AccessCheckable {

    String getType();

    default boolean isValidAccess() {
        return AccessType.isValidType(getType());
    }
}
