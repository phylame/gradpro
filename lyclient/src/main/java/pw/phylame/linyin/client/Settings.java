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

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;

/**
 * Created by Mnelx on 2016-4-13.
 */
public class Settings {
    private Properties properties;

    public Settings(InputStream in) throws IOException {
        Objects.requireNonNull(in, "InputStream for config file is null");
        properties = new Properties();
        properties.load(in);
    }

    boolean get(String name, boolean def) {
        return get(name, def, Boolean::parseBoolean);
    }

    int get(String name, int def) {
        return get(name, def, Integer::parseInt);
    }

    String get(String name, String def) {
        return properties.getProperty(name, def);
    }

    <T> T get(String name, T def, Function<String, T> parser) {
        String value = properties.getProperty(name);
        return value != null ? parser.apply(value) : def;
    }
}
