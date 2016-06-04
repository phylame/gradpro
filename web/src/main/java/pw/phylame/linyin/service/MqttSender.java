/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import pw.phylame.linyin.constants.Constants;
import pw.phylame.linyin.constants.MqttQoS;
import pw.phylame.linyin.util.JsonUtils;

/**
 * @author Peng Wan
 */
@Component
public class MqttSender extends CommonService {
    @Autowired
    private MqttClient mqttClient;

    @Autowired
    private MqttConnectOptions mqttOptions;

    private MqttCallback mqttCallback = new MyMqttCallback();

    private boolean connected = false;

    private boolean ensureConnected() {
        if (connected) {
            return true;
        }
        mqttClient.setCallback(mqttCallback);
        try {
            mqttClient.connect(mqttOptions);
        } catch (MqttException e) {
            logger.error("cannot connect to MQTT broker", e);
            return false;
        }
        connected = true;
        return true;
    }

    public boolean publish(String topic, Object o) {
        try {
            return publish(topic, JsonUtils.toJson(o));
        } catch (JsonProcessingException e) {
            logger.debug("cannot convert " + o + " to josn", e);
            return false;
        }
    }

    public boolean publish(String topic, String message) {
        try {
            return publish(topic, message.getBytes(Constants.ENCODING));
        } catch (UnsupportedEncodingException e) {
            logger.error("cannot using {} to encode {}", Constants.ENCODING, message);
            return false;
        }
    }

    public boolean publish(String topic, byte[] payload) {
        ensureConnected();
        MqttMessage message = new MqttMessage(payload);
        message.setQos(MqttQoS.AT_LEAST_ONCE);
        message.setRetained(true);
        return publish(topic, message);
    }

    public boolean publish(String topic, MqttMessage message) {
        ensureConnected();
        try {
            mqttClient.publish(topic, message);
        } catch (MqttException e) {
            logger.debug("failed to publish message " + message, e);
            return false;
        }
        return true;
    }

    private class MyMqttCallback implements MqttCallback {
        /*
         * (non-Javadoc)
         *
         * @see
         * org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.
         * Throwable)
         */
        @Override
        public void connectionLost(Throwable cause) {
            logger.error("lost connection to MQTT broker");
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.
         * String, org.eclipse.paho.client.mqttv3.MqttMessage)
         */
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            // TODO Auto-generated method stub
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.
         * eclipse.paho.client.mqttv3.IMqttDeliveryToken)
         */
        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            logger.debug("delivery complete for {}", token);
        }
    }
}
