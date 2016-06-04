/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.controller.form;

import lombok.Data;

@Data
class AbstractForm implements AccessCheckable {
    private String type;

    private String message;
}
