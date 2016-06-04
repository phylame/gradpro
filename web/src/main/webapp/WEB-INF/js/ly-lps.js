/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

function postJson(url, success, error) {
    $.ajax({
        url: contextPath + url,
        type: "POST",
        dataType: "json",
        success: success,
        error: error
    });
}

function deleteMessage(messageId, task) {
    postJson("/message/delete/" + messageId, function (response) {
        if (response.state) {
            task();
        } else {
            alert("删除失败：" + response.data);
        }
    });
}

function subscribeTag(tagId, task) {
    do_subscribe("/tag/subscribe/" + tagId, task);
}

function unsubscribeTag(tagId, task) {
    do_unsubscribe("/tag/unsubscribe/" + tagId, task);
}

function subscribeUser(accountId, task) {
    do_subscribe("/user/subscribe/" + accountId, task);
}

function unsubscribeUser(accountId, task) {
    do_unsubscribe("/user/unsubscribe/" + accountId, task);
}

function do_subscribe(url, task) {
    postJson(url, function (response) {
        if (response.state) {
            task();
        } else {
            alert("订阅失败：" + response.data);
        }
    })
}

function do_unsubscribe(url, task) {
    postJson(url, function (response) {
        if (response.state) {
            task();
        } else {
            alert("取消订阅失败：" + response.data);
        }
    })
}
