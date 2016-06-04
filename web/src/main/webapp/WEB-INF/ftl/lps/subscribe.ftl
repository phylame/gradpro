<#import "widgets/html.ftl" as html/>
<#import "widgets/listView.ftl" as listView/>
<#macro itemLimit selid size deftop min curlimit>
  <#if size gt 2>
  <span>显示数目:</span>
  <select id="${selid}" onchange="change_limit()">
    <#if size lt curlimit>
      <#local curlimit=size/>
    </#if>
    <#if size lt deftop>
      <#assign top=size/>
    <#else>
      <#assign top=deftop/>
    </#if>
    <#list min..top as i>
      <option value="${i}" <#if i == curlimit>selected="selected"</#if>>${i}</option>
    </#list>
  </select>
  </#if>
</#macro>
<@html.html pageTitle "subscribe.title"; title>
  <@html.head title>
    <@listView.itemStyle/>
    <@listView.tagUserFunctions/>
  <style type="text/css">
    .region {
    }

    .region h3 {
      border-left: 3px solid #1ABC9C;
      padding-left: 2px;
    }
  </style>
  <script>
    function search_tag() {
      $("#opt_tag").attr("selected", true);
      $("#txt_q").focus();
    }
    function search_user() {
      $("#opt_user").attr("selected", true);
      $("#txt_q").focus();
    }
    function change_limit() {
      window.location.href = "${currentURL}?tagLimit=" + $("#sel_tl").val() + "&merchantLimit=" + $("#sel_ml").val();
    }
  </script>
  </@html.head>
  <@html.body title>
    <#if tags??>
    <div class="region left">
      <h3>订阅商品标签相关消息</h3>
      <ol>
        <#list tags as tag>
          <li class="list-item">
            <@listView.tagItem tag currentUser/>
          </li>
        </#list>
      </ol>
      <#if (tags?size > 1)>
        <hr/>
        <@itemLimit "sel_tl" tagNumber 10 2 tagLimit/>
        <a href="<@spring.url "/tag/list"/>" title="查看全部标签">全部标签</a>
      <span>或</apsn>
        <button onclick="search_tag()" class="button"><@spring.message "common.button.search"/></button>
      </#if>
    </div>
    <#else>
    <div>
      No tag found, <a href="<@spring.url "/tag/new"/>">create new</a>?
    </div>
    </#if>
    <#if merchants??>
    <div class="region right">
      <h3>订阅商家发布的消息</h3>
      <ol>
        <#list merchants as user>
          <li class="list-item">
          ${tag}
      <@listView.userItem user currentUser/>
          </li>
        </#list>
      </ol>
      <#if (merchants?size > 1)>
        <hr/>
        <@itemLimit "sel_ml" merchantNumber 10 2 merchantLimit/>
        <a href="<@spring.url "/user/list"/>" title="查看全部用户">全部用户</a>
      <span>或</apsn>
        <button onclick="search_user()" class="button"><@spring.message "common.button.search"/></button>
      </#if>
    </div>
    <#else>
    <hr/>
    <div>
      No user found, <a href="<@spring.url "/user/register"/>">register new</a>?
    </div>
    </#if>
    <@html.clear/>
  </@html.body>
</@html.html>
