<#import "widgets/html.ftl" as html/>
<#import "widgets/listView.ftl" as listView/>
<@html.html pageTitle "page.results.title"; title>
  <@html.head title>
    <@listView.itemStyle/>
  </@html.head>
  <@html.body title>
    <@listView.body pager pageIndices; item>
    ${item.name}
    </@listView.body>
  </@html.body>
</@html.html>
