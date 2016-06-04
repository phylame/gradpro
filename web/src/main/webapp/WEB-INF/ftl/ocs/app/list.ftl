<#import "widgets/html.ftl" as html/>
<#import "widgets/listView.ftl" as listView/>
<@html.html pageTitle "page.appList.title"; title>
  <@html.head title>
    <@listView.itemStyle/>
    <@listView.appFunctions/>
  </@html.head>
  <@html.body title>
    <@listView.body pager pageIndecies; app>
      <@listView.appItem app currentUser/>
    </@listView.body>
  </@html.body>
</@html.html>
