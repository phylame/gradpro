/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.constants;

/**
 * @author Peng Wan
 */
public interface MqttQoS {
    int AT_MOST_ONCE = 0;
    int AT_LEAST_ONCE = 1;
    int ONLY_ONCE = 2;

    int DEFAULT = AT_LEAST_ONCE;
}
