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

import pw.phylame.linyin.util.HttpUtils;
import pw.phylame.linyin.util.JsonUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static pw.phylame.linyin.constants.Constants.*;
import static pw.phylame.linyin.constants.HttpConstants.*;

class LyUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("h:m:s:S");

    private static final String DEBUG = "debug";
    private static final String ERROR = "error";

    static HttpUtils.Response sendJson(Object o, String url, Map<String, String> props) throws IOException {
        String json = JsonUtils.toJson(o);
        byte[] data = json.getBytes(ENCODING);
        props.put(CONTENT_TYPE, MIME_JSON_TEXT);
        props.put(CONTENT_LENGTH, Integer.toString(data.length));
        HttpUtils.Response response = HttpUtils.sendPost(url, data, props);
        debug("send json {0} to {1}s", json, url);
        if (response.getCode() != 200) {
            debug("the LCS occurred error(s), so stop...");
            return null;
        }
        return response;
    }

    static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    static String ifNotEmpty(String s, String def) {
        return !isEmpty(s) ? s : def;
    }

    static void debug(String msg) {
        cout(DEBUG, msg);
    }

    static void debug(String msg, Object... args) {
        cout(DEBUG, msg, args);
    }

    static void error(String msg, Object... args) {
        cerr(ERROR, msg, args);
    }

    private static void cout(String tag, String msg, Object... args) {
        System.out.println(formatMessage(tag, msg, args));
    }

    private static void cerr(String tag, String msg, Object... args) {
        System.err.println(formatMessage(tag, msg, args));
    }

    private static String formatMessage(String tag, String msg, Object... args) {
        String text = MessageFormat.format(msg, args);
        return String.format("[%s]-[%s]-%s: %s", sdf.format(new Date()), Thread.currentThread().getName(), tag, text);
    }
}
