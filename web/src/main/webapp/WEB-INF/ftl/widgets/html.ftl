<#import "widgets/util.ftl" as util/>
<#import "widgets/header.ftl" as header/>
<#import "widgets/footer.ftl" as footer/>

<#macro css url>
<link href="${url}" rel="stylesheet"/>
</#macro>

<#macro lyCss name>
<link href="<@spring.url "/static/css/${name}.css"/>" rel="stylesheet"/>
</#macro>

<#macro js url>
<script src="${url}"></script>
</#macro>

<#macro lyJs name>
<script src="<@spring.url "/static/js/${name}.js"/>"></script>
</#macro>

<#macro head title>
<head>
  <meta charset="utf-8"/>
  <title>${title}</title>
  <@lyCss "ly-main"/>
  <script>
    // web context path
    contextPath = "<@spring.url ""/>";
  </script>
  <@js "http://lib.sinaapp.com/js/jquery/1.9.1/jquery-1.9.1.min.js"/>
  <@lyJs "ly-main"/>
  <#nested/>
</head>
</#macro>

<#macro body bannerTitle>
<body>
<div class="container">
  <@header.page bannerTitle/>
  <div class="content">
    <div class="wrapper">
      <#nested/>
    </div>
  </div>
  <@footer.page/>
</div>
</body>
</#macro>

<#macro menu>
<div class="context-menu left">
  <ul>
    <#nested/>
  </ul>
</div>
</#macro>

<#macro content>
<div class="content-text left">
  <#nested/>
</div>
</#macro>

<#macro clear>
<div class="clear"></div>
</#macro>

<#macro html pageTitle code>
  <#assign title><@util.text pageTitle code/></#assign>
<!DOCTYPE html>
<html>
  <#nested title/>
</html>
</#macro>

<#macro ifLogged>
  <#if currentUser??>
    <#nested currentUser/>
  </#if>
</#macro>

<#macro ifCurrentUser user>
  <#if currentUser?? && currentUser.accountId == user.accountId>
    <#nested />
  </#if>
</#macro>

<#macro notCurrentUser user>
  <#if currentUser?? && currentUser.accountId != user.accountId>
    <#nested />
  </#if>
</#macro>

<#macro button func title="" class="button">
<button type="button" class="${class}" title="${title}" onclick="${func}">
  <#nested />
</button>
</#macro>

<#macro forError>
  <#if errorText??>
    <#nested errorText/>
  </#if>
</#macro>