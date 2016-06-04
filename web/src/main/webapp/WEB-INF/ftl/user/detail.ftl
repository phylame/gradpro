<#import "widgets/html.ftl" as html/>
<#import "widgets/form.ftl" as form/>
<#import "widgets/items.ftl" as items/>
<@html.html pageTitle "user.details.title"; title>
  <@html.head title>
    <@html.lyCss "table"/>
    <@html.lyCss "ly-user"/>
    <@html.lyJs "ly-lps"/>
  <script>
    function subscribe_user() {
      subscribeUser(${user.accountId}, function () {
        window.location.reload();
      });
    }
    function unsubscribe_user() {
      unsubscribeUser(${user.accountId}, function () {
        window.location.reload();
      });
    }
  </script>
  </@html.head>
  <@html.body title>
    <@html.menu>
      <#if currentUser.accountId == user.accountId>
      <li><a href="<@spring.url "/user/edit"/>"><@spring.message "user.details.edit"/></a></li>
      <li><a href="<@spring.url "/user/settings"/>"><@spring.message "user.details.settings"/></a></li>
      <li><a href="<@spring.url "/user/tags"/>"><@spring.message "user.details.tags"/></a>
      <li><a href="<@spring.url "/user/subscription"/>" title="查看我订阅的内容">我的订阅</a></li>
      <li><a href="<@spring.url "/message/list/merchant/${user.accountId}"/>" title="查看我已发的消息">我的消息</a></li>
      <#else>
      <li><a href="<@spring.url "/message/list/merchant/${user.accountId}"/>" title="查看用户已发的消息">他的消息</a></li>
      </#if>
    <li><a href="<@spring.url "/user/list"/>" title="查看全部用户">全部用户</a></li>
    </@html.menu>
    <@html.content>
    <table id="h-table" style="margin:0;">
      <tr>
        <@form.tip "user.field.accountId"/>
        <td>${user.accountId}</td>
      </tr>
      <tr>
        <@form.tip "common.field.name"/>
        <td>${user.name}</td>
      </tr>
      <tr>
        <@form.tip "user.field.telephone"/>
        <td>${user.telephone}</td>
      </tr>
      <#if user.location??>
        <tr>
          <@form.tip "user.field.address"/>
          <td><@items.locationLink user.location/></a></td>
        </tr>
      </#if>
      <tr>
        <@form.tip "user.field.tags"/>
        <td style="white-space:nowrap">
          <#if user.tags?size != 0>
            <ul class="user-tags">
              <#list user.tags as tag>
                <li><@items.tagLink tag/></li>
              </#list>
            </ul>
          <#else>
            (无)
          </#if>
        </td>
      </tr>
      <tr>
        <@form.tip "common.field.subNumber"/>
        <td style="white-space: nowrap;"><@items.subscriberNumber user "user"/></td>
      </tr>
      <tr>
        <@form.tip "common.field.intro"/>
        <td>${user.intro}</td>
      </tr>
    </table>
      <@html.notCurrentUser user>
      <hr/>
      <div>
        <#if user.subscribed>
          <@html.button "unsubscribe_user()" "取消接收他的消息" >取消订阅</@html.button>
        <#else>
          <@html.button "subscribe_user()" "接收他的消息" >订 阅</@html.button>
        </#if>
      </div>
      </@html.notCurrentUser>
    </@html.content>
    <@html.clear/>
  </@html.body>
</@html.html>
