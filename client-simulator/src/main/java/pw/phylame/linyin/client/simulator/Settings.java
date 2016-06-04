/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.client.simulator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;

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
