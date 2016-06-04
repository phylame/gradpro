<#import "widgets/html.ftl" as html/>
<#import "widgets/listView.ftl" as listView/>
<@html.html pageTitle "page.tagList.title"; title>
  <@html.head title>
    <@listView.itemStyle/>
    <@listView.tagFunctions/>
  </@html.head>
  <@html.body title>
  <span>或 </span><a href="<@spring.url "/tag/new"/>" title="新建标签">新建</a>
    <@listView.body pager pageIndecies; tag>
      <@listView.tagItem tag currentUser/>
    </@listView.body>
  </@html.body>
</@html.html>
