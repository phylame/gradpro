/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * @author Peng Wan
 */
public class MyMqttConnectOptions extends MqttConnectOptions {
    public void setPassword(String password) {
        setPassword(password.toCharArray());
    }
}
