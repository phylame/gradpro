<#import "widgets/html.ftl" as html/>
<#import "widgets/items.ftl" as items/>
<@html.html pageTitle "user.tags.title"; title>
  <@html.head title>
    <@html.lyCss "table"/>
    <@html.lyJs "ly-table"/>
  <script>

    insert_mode = false;

    function insertRow(tags) {
      var options = "", len = tags.length;
      for (var i = 0; i < len; ++i) {
        var tag = tags[i];
        options += '<option title="{intro}" value="{tagId}">{name}</option>'.format(tag);
      }
      var tr = '<tr name="new-tr">\
        <td><input type="checkbox" name="ckb" value="${tag.tagId}"/></td>\
        <td>\
          <select name="sel-tag">'
        + options +
        '</select>\
      </td>\
      <td>${currentUser.name}</td>\
        <td>0</td>\
        <td>0</td>\
        <td><input name="txt-intro"/></td>\
        </tr>';
      appendRow("v-table", -1, tr);
      $("#btn-finish").css("display", "inline");
      insert_mode = true;
    }

    function appendTag() {
      // get all tags
      $.ajax({
        url: "<@spring.url "/ocs/tag/all"/>",
        type: "POST",
        dataType: "json",
        success: function (r) {
          if (!r.state) {
            alert("Cannot fetch tags from server!");
          } else {
            insertRow(r.data);
          }
        }
      });
    }

    function finishAdd() {
      $("tr[name='new-tr']").each(function () {
        var sel = $(this).find("select[name='sel-tag']"), txt = $(this).find("input[name='txt-intro']");
        $.ajax({
          url: "<@spring.url "/user/tags/append/"/>" + sel.val(),
          type: "POST",
          data: {location: 0, intro: txt.val()},
          dataType: "json",
          success: function (r) {
            if (r.state) {
              insert_mode = false;
              $("#btn-finish").css("display", "none");
              location.reload();
            } else {
              alert(r.data);
            }
          }
        });
      });
    }

    function removeTags() {
      removeRows('ckb', function (ckb, del) {
        var tagId = ckb.value;
        if (insert_mode) {    // in add mode
          del();
          return;
        }
        var xhr = $.ajax({
          url: "<@spring.url "/user/tags/remove/"/>" + tagId,
          type: "POST",
          dataType: "json",
          success: function (r) {
            if (r.state) {
              del();
            } else {
              alert(r.message);
            }
          }
        });
      });
    }

    state = {checked: false};
  </script>
  </@html.head>
  <@html.body title>
    <@html.menu>
    <li><a href="<@spring.url "/user/detail/${currentUser.accountId}"/>">返回详情</a></li>
    <li><a href="<@spring.url "/tag/list"/>">全部标签</a></li>
    </@html.menu>
    <@html.content>
    <table id="v-table">
      <thead>
      <tr style="text-align:left;">
        <th></th>
        <th><@spring.message "common.field.name"/></th>
        <th><@spring.message "common.field.creator"/></th>
        <th><@spring.message "common.field.subNumber"/></th>
        <th><@spring.message "common.field.msgNumber"/></th>
        <th><@spring.message "common.field.intro"/></th>
      </tr>
      </thead>
      <#list tags as tag>
        <tr <#if tag_index % 2 == 1>class="alt"</#if>>
          <td><input type="checkbox" name="ckb" value="${tag.tagId}"/></td>
          <td><@items.tagLink tag/></td>
          <td><@items.userLink tag.creator "[内置]"/></td>
          <td style="white-space:nowrap;"><@items.subscriberNumber tag "tag" false/></td>
          <td style="white-space:nowrap;"><@items.messageNumber tag "tag" false/></td>
          <td>${tag.intro}</td>
        </tr>
      </#list>
    </table>
    <hr/>
    <div>
      <button type="button" class="button" onclick="toggleAll('ckb', state)">全 选</button>
      <button type="button" class="button" onclick="removeTags()">
        <@spring.message "common.button.delete"/>
      </button>
      <button type="button" class="button" onclick="appendTag()">添 加</button>
      <button id="btn-finish" type="button" class="button" style="display:none" onclick="finishAdd()">完 成</button>
    </div>
    </@html.content>
    <@html.clear/>
  </@html.body>
</@html.html>
