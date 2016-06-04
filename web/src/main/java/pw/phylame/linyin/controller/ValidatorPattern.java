/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.controller;

/**
 * Created by Mnelx on 2016-4-17.
 */
public interface ValidatorPattern {
    String USERNAME = "^(\\w){4,24}$";
    String PASSWORD = "^(\\S){6,16}$";
    String TELEPHONE = "(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{7,14}";
}
