/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

/**
 * Utilities for table.
 */

/**
 * Appends new tr in table.
 *
 * @param tblId
 *            id of the table
 * @param rowIdx
 *            index of row, 0 -> first, 1 -> second, -1 -> last
 * @param trHtml
 *            tr html content
 */
function appendRow(tblId, rowIdx, trHtml) {
    var $tr = $("#" + tblId + " tr").eq(rowIdx);
    if ($tr.size() == 0) {
        alert("指定的 table id 或行数不存在！");
        return;
    }
    $tr.after(trHtml);
}

/**
 * Removes all checked rows.
 *
 * @param name
 *            name of the check boxes
 */
function removeRows(name, task) {
    var ckbs = $("input[name=" + name + "]:checked");
    if (ckbs.size() == 0) {
        alert("要删除指定行，需选中要删除的行！");
        return;
    }
    ckbs.each(function () {
        var ckb = this;
        task(this, function () {
            $(ckb).parent().parent().remove();
        });
    });
}

/**
 * Toggle selection of check boxes.
 *
 * @param name
 *            name of check boxes
 * @param state
 */
function toggleAll(name, state) {
    state.checked = !state.checked;
    $('[name=' + name + ']:checkbox').each(function () {
        this.checked = state.checked;
    })
}
