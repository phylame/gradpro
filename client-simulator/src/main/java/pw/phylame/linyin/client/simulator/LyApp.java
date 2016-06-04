/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.client.simulator;

import static pw.phylame.linyin.util.MiscUtils.asMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import pw.phylame.linyin.constants.ErrorCode;
import pw.phylame.linyin.constants.HttpConstants;
import pw.phylame.linyin.constants.MqttQoS;
import pw.phylame.linyin.pojo.GeoLocation;
import pw.phylame.linyin.pojo.ocs.TmlData;
import pw.phylame.linyin.pojo.ocs.TmrData;
import pw.phylame.linyin.util.DateUtils;
import pw.phylame.linyin.util.HttpUtils;
import pw.phylame.linyin.util.JsonUtils;
import pw.phylame.linyin.util.MiscUtils.MapBuilder;
import pw.phylame.linyin.util.StringUtils;

/**
 * @author Peng Wan
 */
public class LyApp {
    private Settings settings;
    private String clientId;
    private long accountId;

    private boolean loadSettings() {
        try (InputStream in = LyApp.class.getResourceAsStream("/lyclient/settings.properties")) {
            settings = new Settings(in);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        clientId = settings.get("clientId", null);
        if (StringUtils.isBlank(clientId)) {
            System.err.println("not found valid 'clientId' in settings");
            return false;
        }
        return true;
    }

    private boolean loginToLy() {
        String username = settings.get("username", "test"), password = settings.get("password", "123456");
        String path = joinPath("/ocs/user/login", false);
        Map<String, Object> result;
        try {
            result = postJson(MapBuilder.newMap().put("username", username).put("password", password).toMap(), path,
                    asMap());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        int code = (int) result.get("code");
        switch (code) {
        case ErrorCode.OK:
            accountId = (int) result.get("data");
            System.out.println("login success and get my account ID: " + accountId);
            return true;
        default:
            break;
        }
        return false;
    }

    private void logout() {
        System.out.println("logout from ly OCS");
        try {
            HttpUtils.sendPost(joinPath("/ocs/user/logout", true), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> getSubscriptions() {
        String path = joinPath("/ocs/user/subscriptions", true);
        Map<String, Object> result;
        try {
            result = postJson(1, path, asMap());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        int code = (int) result.get("code");
        switch (code) {
        case ErrorCode.OK:
            return (List<String>) result.get("data");
        default:
            break;
        }
        return Collections.emptyList();
    }

    private void connectMqtt() {
        try {
            String brokerURL = settings.get("mqtt.brokerURL", null), clientId = settings.get("mqtt.clientId", "demo");
            MqttClient mqttClient = new MqttClient(brokerURL, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(null);
            options.setPassword(null);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // TODO Auto-generated method stub
                    System.out.println("receive message " + message + " for topic " + topic);
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
            mqttClient.connect(options);
            for (String topic : getSubscriptions()) {
                mqttClient.subscribe(topic, MqttQoS.AT_MOST_ONCE);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private TmrData getRequirement() {
        TmrData tmr = new TmrData();
        tmr.setStartTime(DateUtils.calculateDateByNow('h', -1));
        tmr.setEndTime(DateUtils.calculateDateByNow('h', 2));
        tmr.setTags(Arrays.asList(1L, 2L, 3L));
        tmr.setMessage("有优惠的更好");
        return tmr;
    }

    private boolean sendTmr() {
        String path = joinPath("/ocs/tmr/new", true);
        Map<String, Object> result;
        try {
            result = postJson(getRequirement(), path, asMap());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        switch ((int) result.get("code")) {
        case ErrorCode.OK:
            System.out.println("send requirement success");
            return true;
        case ErrorCode.NO_SUCH_ACCOUNT:
            System.out.println("Maybe I had not registered an account");
            break;
        case ErrorCode.NOT_LOGGED_IN:
            System.out.println("I had not logged into system");
            break;
        case ErrorCode.NO_REQUIREMENT_TAGS:
            System.out.println("I had not set any requirement tags");
            break;
        case ErrorCode.REQUIREMENT_NOT_LIVING:
            System.out.println("The requirement is not living");
            break;
        case ErrorCode.USER_NOT_SUBSCRIBED:
            System.out.println("I had not subscribed anything, so stop send my location");
            break;
        }
        return false;
    }

    private TmlData getLocation() {
        return new TmlData(accountId, GeoLocation.currentLocation());
    }

    private void sendTml() {
        String path = joinPath("/ocs/tml", true);
        Map<String, Object> result;
        try {
            result = postJson(getLocation(), path, asMap());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        switch ((int) result.get("code")) {
        case ErrorCode.OK:
            break;
        case ErrorCode.REQUIREMENT_NOT_LIVING:
            throw new RuntimeException("the requirement is expired");
        default:
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            @SuppressWarnings("unchecked")
            List<Object> merchants = (List<Object>) result.get("data");
            System.out.println("found merchant(s) nearby, do you wanna have a look:");
            int choice = -0;
            do {
                int i = 1;
                for (Object merchant : merchants) {
                    System.out.printf("  %2d): %s\n", i++, merchant);
                }
                System.out.println("  -1): if you do not like them");
                System.out.print("which merchant you wanna go: ");
                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    // TODO: handle exception
                } finally {
                    scanner.reset();
                }
            } while (choice <= 0 && choice != -1 || choice > merchants.size());
            System.out.println(choice);
            if (choice == -1) {
                System.out.println("You ignores those merchants");
            } else {
                System.out.printf("Now, you can go to '%s' for more information\n", merchants.get(choice - 1));
                System.out.println("Now, stop send TML to LCS. If you want resend, please restart");
                throw new RuntimeException();
            }
        }
    }

    private void start() {
        if (!loadSettings()) {
            return;
        }
        if (!loginToLy()) {
            return;
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::logout));
        connectMqtt();
        sendTmr();
        long delay = settings.get("notify.delay", 1000);
        try {
            for (;;) {
                sendTml();
                Thread.sleep(delay);
            }
        } catch (RuntimeException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String joinPath(String base, boolean useAccountId) {
        if (useAccountId) {
            return base + '/' + clientId + '/' + accountId;
        } else {
            return base + '/' + clientId;
        }
    }

    private static String ENCODING = "UTF-8";

    private Map<String, Object> postJson(Object o, String path, Map<String, String> props) throws IOException {
        String json = JsonUtils.toJson(o);
        byte[] data = json.getBytes(ENCODING);
        props.put(HttpConstants.CONTENT_TYPE, HttpConstants.MIME_JSON_TEXT);
        props.put(HttpConstants.CONTENT_LENGTH, Integer.toString(data.length));
        String url = settings.get("lyhost", null) + path;
        HttpUtils.Response response = HttpUtils.sendPost(url, data, props);
        System.out.printf("send json %s to %s\n", json, url);
        if (response.getCode() != 200) {
            System.out.println("the LCS occurred error(s), so stop...");
            return null;
        } else if (!response.getContentType().startsWith(HttpConstants.MIME_JSON_TEXT)) {
            System.out.println("response not return JOSN data: " + response.getContentType());
            return null;
        }
        return JsonUtils.<String, Object>toMap(response.getContent());
    }

    public static void main(String[] args) {
        new LyApp().start();
    }
}
