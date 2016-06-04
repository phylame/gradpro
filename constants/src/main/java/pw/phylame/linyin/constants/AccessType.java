/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.constants;

import java.util.Arrays;
import java.util.List;

/**
 * @author Peng Wan
 */
public interface AccessType {
    String WEB = "W";
    String MOBILE = "M";
    String ADMIN = "A";
    String OTHER = "O";

    List<String> ALL_TYPES = Arrays.asList(WEB, MOBILE, ADMIN, OTHER);

    static boolean isValidType(String type) {
        return ALL_TYPES.contains(type);
    }
}
