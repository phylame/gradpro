<#import "widgets/html.ftl" as html/>
<@html.html pageTitle "about.title"; title>
  <@html.head title/>
  <@html.body title>
    <#include "home/about.txt" parse=false encoding="UTF-8"/>
  </@html.body>
</@html.html>
