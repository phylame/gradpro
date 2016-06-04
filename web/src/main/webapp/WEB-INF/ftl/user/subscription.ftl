<#import "widgets/html.ftl" as html/>
<#import "widgets/items.ftl" as items/>
<#macro region name elements func type>
<div class="region">
  <h3>${name}</h3>
  <#if !elements?? || elements?size == 0>
    <span>无已订阅的${name}，查找<a href="<@spring.url "/subscribe"/>">订阅</a>？</span>
  <#else>
    <span>已订阅 ${elements?size} 个${name}</span>
    <ol id="${type}-ol">
      <#list elements as item>
        <#assign obj_id><#if type = "tag">${item.tagId}<#else>${item.accountId}</#if></#assign>
        <li id="${type}-${obj_id}">
          <#nested item/> | 时间：${item.time?datetime?string.long_short}
          <@html.button "${func}(${obj_id}, '${type}_${obj_id}')" "取消关注">取 消</@html.button>
        </li>
      </#list>
    </ol>
  </#if>
  <hr/>
</div>
</#macro>
<@html.html pageTitle "user.subscription.title"; title>
  <@html.head title>
  <style>
    .region {
    }

    .region h3 {
      margin: 0 auto;
      padding: 0;
    }

    .region ol {
      margin: 0 auto;
    }

    .region li {
      padding: 5px 0;
    }
  </style>
    <@html.lyJs "ly-lps"/>
  <script>
    function unsubscribe_tag(tag_id, li_id) {
      unsubscribeTag(tag_id, function () {
        $("#" + li_id).remove();
        if ($("tag-ol").size() == 0) {
          location.reload();
        }
      });
    }

    function unsubscribe_user(user_id, li_id) {
      unsubscribeUser(user_id, function () {
        $("#" + li_id).remove();
        if ($("user-ol").size() == 0) {
          location.reload();
        }
      });
    }
  </script>
  </@html.head>
  <@html.body title>
    <@html.menu>
    <li><a href="<@spring.url "/user/detail/${currentUser.accountId}"/>">返回详情</a></li>
    <li><a href="<@spring.url "/tag/list"/>">全部标签</a></li>
    <li><a href="<@spring.url "/user/list"/>">全部商家</a></li>
    </@html.menu>
    <@html.content>
      <@region "标签" tags "unsubscribe_tag" "tag"; tag>
        <@items.tagLink tag/> | <@items.messageNumber tag "tag"/>
      </@region>
      <@region "商家" merchants "unsubscribe_user" "user"; merchant>
        <@items.userLink merchant/> | <@items.messageNumber merchant "merchant"/>
      </@region>
    </@html.content>
    <@html.clear/>
  </@html.body>
</@html.html>
