<#import "widgets/html.ftl" as html/>
<#import "widgets/items.ftl" as items/>
<#import "widgets/pager.ftl" as paging/>

<#-- set to the li -->
<#macro itemStyle>
  <@html.lyCss "ly-list"/>
  <@html.lyCss "ly-pager"/>
</#macro>

<#macro _subscribe_tag>
function subscribe_tag(tagId, subscribed) {
var task = function() {
window.location.reload();
};
if (!subscribed) {
subscribeTag(tagId, task);
} else {
unsubscribeTag(tagId, task);
}
}
</#macro>
<#macro tagFunctions>
  <@html.lyJs "ly-lps"/>
<script>
<@_subscribe_tag/>
</script>
</#macro>

<#macro _subscribe_user>
function subscribe_user(userId, subscribed) {
var task = function() {
window.location.reload();
};
if (!subscribed) {
subscribeUser(userId, task);
} else {
unsubscribeUser(userId, task);
}
}
</#macro>
<#macro userFunctions>
  <@html.lyJs "ly-lps"/>
<script>
<@_subscribe_user/>
</script>
</#macro>

<#macro messageFunctions>
  <@html.lyJs "ly-lps"/>
<script>
  function delete_message(messageId) {
    deleteMessage(messageId, function () {
      window.location.reload();
    });
  }
</script>
</#macro>

<#macro tagUserFunctions>
  <@html.lyJs "ly-lps"/>
<script>
<@_subscribe_tag/>
<@_subscribe_user/>
</script>
</#macro>

<#macro appFunctions>
</#macro>

<#macro tagItem tag currentUser>
<div class="link">
  <@items.tagLink tag/>
</div>
<div class="content">
  <div class="left">
    <#if tag.creator??>
      <span>创建者:</span>
      <@items.userLink tag.creator/>
      |
    </#if>
    <@items.userNumber tag/>
    |
    <@items.subscriberNumber tag "tag"/>
    |
    <@items.messageNumber tag "tag"/>
    <div class="nowrap">简介：<span class="intro">${tag.intro!"(无内容)"}</span></div>
  </div>
  <#if currentUser??>
    <div class="right">
      <@items.subscribe tag.subscribed "subscribe_tag(${tag.tagId}, ${tag.subscribed?string})" tag.name "left button"/>
    </div>
  </#if>
  <@html.clear/>
</div>
</#macro>

<#macro userItem user currentUser>
<div class="link">
  <@items.userLink user/>
</div>
<div class="content">
  <div class="left">
    <#if user.tags?size !=0>
      <span class="left">标签：</span>
      <@items.tagList user.tags "inline-list left"/>
    </#if>
    <div class="left">
      <@items.subscriberNumber user "user"/>
      |
      <@items.messageNumber user "user"/>
    </div>
    <@html.clear/>
    <div class="nowrap">简介：<span class="intro">${user.intro!"(无内容)"}</span></div>
  </div>
  <@html.notCurrentUser user>
    <div class="right">
      <@items.subscribe user.subscribed "subscribe_user(${user.accountId}, ${user.subscribed?string})" user.name "left button"/>
    </div>
  </@html.notCurrentUser>
  <@html.clear/>
</div>
</#macro>

<#macro messageItem message currentUser>
<div class="link">
  <@items.messageLink message/>
</div>
<div class="content">
  <div class="left">
    <div style="font-size:13px;">
      <#if !for_all??>
        <#if !for_vendor??>
          <span>发布者:</span>
          <@items.userLink message.vendor/>
          |
        </#if>
        <#if !for_tag??>
          <span>标签:</span>
          <@items.tagLink message.tag/>
          |
        </#if>
      </#if>
      <span>状态:</span>
      <#if !message.started>
        <span style="color:#2ECC71">未开始</span>
      <#elseif message.stopped>
        <span style="color:#1ABC9C">已结束</span>
      <#else>
        <span style="color:#F1C40F">活动中</span>
      </#if>
    </div>
    <div class="nowrap">内容：<span class="intro">${message.content!"(无内容)"}</span></div>
  </div>
  <@html.ifCurrentUser message.vendor>
    <div class="right">
      <button type="button" title="删除此条消息" onclick="delete_message('${message.messageId}')" class="button"
              style="color:#E74C3C;font-weight:bold;">删 除
      </button>
    </div>
  </@html.ifCurrentUser>
  <@html.clear/>
</div>
</#macro>

<#macro appItem app currentUser>
<div class="link">
  <@items.appLink app/>
</div>
<div class="content">
  <div class="left">
    <#if app.author??>
      <span>创建者:</span>
      <@items.userLink app.author/>
      |
    </#if>
    <span>平台：</span>${app.platformName}
    <div class="nowrap">简介：<span class="intro">${app.description!"(无内容)"}</span></div>
  </div>
  <@html.clear/>
</div>
</#macro>

<#macro body pager pageIndices class="" prefix="">
<hr/>
  <#if !pager?? || pager.totalElements == 0>
  <span>无内容</span>
  <#else>
  <ol>
    <#list pager.contents as item>
      <li id="${prefix}li_${item_index + 1}" class="${class} list-item">
        <#nested item item_index + 1 />
      </li>
    </#list>
  </ol>
    <@paging.page pager pageIndices />
  </#if>
</#macro>
