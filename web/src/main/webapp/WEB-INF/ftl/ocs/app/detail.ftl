<#import "widgets/html.ftl" as html/>
<@html.html pageTitle "app.detail.title"; title>
  <@html.head title>
  </@html.head>
  <@html.body title>
  your app ID is ${app.clientId}
  </@html.body>
</@html.html>
