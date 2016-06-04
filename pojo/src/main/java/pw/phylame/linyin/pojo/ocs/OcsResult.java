/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.pojo.ocs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO for response of OCS controller.
 *
 * @author Peng Wan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcsResult {
    /**
     * Code of the result of OCS.
     */
    private int code;

    /**
     * Additional data of response.
     */
    private Object data;
}
