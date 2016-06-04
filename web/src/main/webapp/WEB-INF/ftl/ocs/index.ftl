<#import "widgets/html.ftl" as html/>
<@html.html "OCS 服务" "ocs.title"; title>
  <@html.head title>
  </@html.head>
  <@html.body title>
  <div style="margin:0 auto;">
    <ul>
      <li><a href="<@spring.url "/ocs/api"/>">查看 API</a></li>
      <li><a href="<@spring.url "/ocs/app/list"/>">全部 App</a></li>
      <#if currentUser??>
        <li><a href="<@spring.url "/ocs/app/list/author/${currentUser.accountId}"/>">我的 App</a></li>
      </#if>
      <li><a href="<@spring.url "/ocs/app/new"/>">创建 App</a></li>
    </ul>
  </div>
  </@html.body>
</@html.html>
