<#import "widgets/html.ftl" as html/>
<#import "widgets/listView.ftl" as listView/>
<@html.html pageTitle "page.userList.title"; title>
  <@html.head title>
    <@listView.itemStyle/>
    <@listView.userFunctions/>
  </@html.head>
  <@html.body title>
    <@listView.body pager pageIndecies; user>
      <@listView.userItem user currentUser/>
    </@listView.body>
  </@html.body>
</@html.html>
