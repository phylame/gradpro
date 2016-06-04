<#import "widgets/html.ftl" as html/>
<#import "widgets/form.ftl" as form/>
<#import "widgets/items.ftl" as items/>
<@html.html pageTitle "tag.details.title"; title>
  <@html.head title>
    <@html.lyCss "table"/>
    <@html.lyJs "ly-lps"/>
  <script>
    function subscribe_tag() {
      subscribeTag(${tag.tagId}, function () {
        window.location.reload();
      });
    }
    function unsubscribe_tag() {
      unsubscribeTag(${tag.tagId}, function () {
        window.location.reload();
      });
    }
  </script>
  </@html.head>
  <@html.body title>
    <@html.menu>
      <@html.ifLogged>
      <li><a href="<@spring.url "/tag/new"/>" title="创建新标签">创建</a></li>
      </@html.ifLogged>
      <@html.ifCurrentUser tag.creator>
      <li><a href="<@spring.url "/tag/edit/${tag.tagId}"/>" title="编辑标签信息">编辑</a></li>
      </@html.ifCurrentUser>
    <li><a href="<@spring.url "/user/list/tag/${tag.tagId}"/>" title="查看&quot;${tag.name}&quot;相关商家">相关商家</a></li>
    <li><a href="<@spring.url "/tag/list"/>" title="查看全部标签">全部标签</a></li>
    </@html.menu>
    <@html.content>
    <table id="h-table" style="margin:0;">
      <tr>
        <@form.tip "common.field.name"/>
        <td>${tag.name}</td>
      </tr>
      <#if tag.creator??>
        <tr>
          <@form.tip "common.field.creator"/>
          <td><@items.userLink tag.creator/></td>
        </tr>
      </#if>
      <tr>
        <@form.tip "common.field.subNumber"/>
        <td style="white-space:nowrap;"><@items.subscriberNumber tag "tag"/></td>
      </tr>
      <tr>
        <@form.tip "common.field.msgNumber"/>
        <td style="white-space:nowrap;"><@items.messageNumber tag "tag"/></td>
      </tr>
      <tr>
        <@form.tip "common.field.intro"/>
        <td>${tag.intro}</td>
      </tr>
    </table>
      <#if currentUser??>
      <hr/>
      <div>
        <#if tag.subscribed>
          <@html.button "unsubscribe_tag()" "取消接收相关消息" >取消订阅</@html.button>
        <#else>
          <@html.button "subscribe_tag()" "接收相关消息" >订 阅</@html.button>
        </#if>
      </div>
      </#if>
    </@html.content>
    <@html.clear/>
  </@html.body>
</@html.html>
