/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pw.phylame.linyin.constants.Constants;

/**
 * @author Peng Wan
 */
public class DateUtils {
    private static final SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);

    public static String format(Date date) {
        return sdf.format(date);
    }

    public static Date now() {
        return new Date();
    }

    public static boolean beforeNow(Date date) {
        return date.before(now());
    }

    public static boolean afterNow(Date date) {
        return date.after(now());
    }

    public static Date calculateDate(Date date, char type, int amount) {
        int field;
        switch (type) {
            case 'y':
            case 'Y':
                field = Calendar.YEAR;
                break;
            case 'm':
            case 'M':
                field = Calendar.MONTH;
                break;
            case 'd':
            case 'D':
                field = Calendar.DAY_OF_MONTH;
                break;
            case 'h':
            case 'H':
                field = Calendar.HOUR_OF_DAY;
                break;
            case 'n':
            case 'N':
                field = Calendar.MINUTE;
                break;
            case 's':
            case 'S':
                field = Calendar.SECOND;
                break;
            default:
                throw new IllegalArgumentException("Invalid field type: " + type);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    public static Date calculateDateByNow(char type, int amount) {
        return calculateDate(now(), type, amount);
    }
}
