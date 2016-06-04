/*
 * Copyright 2014-2016 Peng Wan <phylame@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pw.phylame.linyin.client;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import pw.phylame.gaf.ixin.IxinUtilities;
import pw.phylame.linyin.pojo.User;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static pw.phylame.linyin.client.LyUtils.debug;
import static pw.phylame.linyin.client.LyUtils.ifNotEmpty;

public class LyClient {
    static Form form;

    Settings settings;
    String clientId;
    User user;


    private LyClient() throws IOException {
        settings = new Settings(LyClient.class.getResourceAsStream("/lyclient.properties"));
        clientId = settings.get("clientId", "100");
    }

    private MqttConnectOptions getMqttOptions() {
        MqttConnectOptions options = new MqttConnectOptions();

        options.setMqttVersion(settings.get("mqtt.version", 0));
        options.setUserName(ifNotEmpty(settings.get("mqtt.username", null), null));
        options.setPassword(settings.get("mqtt.password", "").toCharArray());
        options.setCleanSession(settings.get("mqtt.cleanSession", true));
        options.setConnectionTimeout(settings.get("mqtt.connectionTimeout", 10));
        options.setKeepAliveInterval(settings.get("mqtt.keepAliveInterval", 20));
        return options;
    }

    void connectMqttServer() {
        String brokerURL = settings.get("mqtt.brokerURL", "tcp://localhost:1883");
        try {
            MqttClient mqttClient = new MqttClient(brokerURL, clientId, new MemoryPersistence());
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // TODO Auto-generated method stub
                    System.out.println("receive message " + message + "for topic " + topic);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // TODO Auto-generated method stub
                    System.out.println("deliveryComplete: " + token.isComplete());
                }

                @Override
                public void connectionLost(Throwable cause) {
                    // TODO Auto-generated method stub
                    cause.printStackTrace();
                }
            });
            MqttConnectOptions options = getMqttOptions();
            debug("connecting to MQTT server {0} with options {1}", brokerURL, options);
            mqttClient.connect(options);
            debug("connected successfully.");
        } catch (MqttException e) {
            GuiUtils.error("cannot connect to MQTT server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void login() {
        String username = settings.get("username", "lmzp");
        String password = settings.get("password", "lfm0075");

        debug("logging into Lyweb with {0} and {1}", username, password);
    }

    void logout() {

    }

    private void run() {
        connectMqttServer();
        login();
    }

    public static void main(String[] args) throws Exception {
        LyClient client = new LyClient();
        IxinUtilities.setGlobalFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 14));
        IxinUtilities.setAntiAliasing(true);
        SwingUtilities.invokeAndWait(() -> {
            form = new Form(client);
            form.setVisible(true);
        });
        client.run();
    }
}
