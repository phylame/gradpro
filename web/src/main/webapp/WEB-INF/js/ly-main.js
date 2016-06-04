/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

function notEmpty(s) {
    return s != null && s.length > 0;
}

function lessThan(n) {
    return function (s) {
        return s != null && s.length < n;
    }
}

function moreThan(n) {
    return function (s) {
        return s != null && s.length > n;
    }
}

function inRange(min, max) {
    return function (s) {
        return s != null && s.length > min && s.length < max;
    }
}

function validateField(field, validator, error, fallback) {
    return validator(field.value) || (fallback && fallback(field, error)) || false;
}

String.prototype.format = function (args) {
    if (arguments.length > 0) {
        var result = this;
        if (arguments.length == 1 && typeof (args) == "object") {
            for (var key in args) {
                var reg = new RegExp("({" + key + "})", "g");
                result = result.replace(reg, args[key]);
            }
        } else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] == undefined) {
                    return "";
                } else {
                    var reg = new RegExp("({[" + i + "]})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
        return result;
    } else {
        return this;
    }
};
