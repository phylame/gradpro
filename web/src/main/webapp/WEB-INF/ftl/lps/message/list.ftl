<#import "widgets/html.ftl" as html/>
<#import "widgets/listView.ftl" as listView/>
<@html.html pageTitle "page.messageList.title"; title>
  <@html.head title>
    <@listView.itemStyle/>
    <@listView.messageFunctions/>
  </@html.head>
  <@html.body title>
    <@listView.body pager pageIndecies; message>
      <@listView.messageItem message currentUser/>
    </@listView.body>
  </@html.body>
</@html.html>
