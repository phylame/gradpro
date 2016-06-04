<#import "widgets/util.ftl" as util/>

<#macro userLink user default>
  <#if user??>
  <a href="<@spring.url "/user/detail/${user.accountId}"/>"
     title="<@util.tr "item.user.linkTip" user.name/>">${user.name}</a>
  <#else>
  ${default}
  </#if>
</#macro>

<#macro tagLink tag default>
  <#if tag??>
  <a href="<@spring.url "/tag/detail/${tag.tagId}"/>" title="<@util.tr "item.tag.linkTip" tag.name/>">${tag.name}</a>
  <#else>
  ${default}
  </#if>
</#macro>

<#macro messageLink message default>
  <#if message??>
  <a href="<@spring.url "/message/detail/${message.messageId}"/>"
     title="<@util.tr "item.message.linkTip" message.title/>">${message.title}</a>
  <#else>
  ${default}
  </#if>
</#macro>

<#macro appLink app default>
  <#if app??>
  <a href="<@spring.url "/ocs/app/detail/${app.clientId}"/>"
     title="<@util.tr "item.app.linkTip" app.name/>">${app.name}</a>
  <#else>
  ${default}
  </#if>
</#macro>

<#macro tagList tags class>
<ul class="${class}">
  <#list tags as tag>
    <#if tag_index gt 3>
      <li><span class="nowrap"> ... |&nbsp;</span></li>
      <#break/>
    </#if>
    <li>
      <span class="nowrap"><@tagLink tag/><#if tag_index != tags?size - 1>/ <#else> | </#if></span>
    </li>
  </#list>
</ul>
</#macro>

<#macro subscriberNumber obj type extText=true>
  <#if obj.subNumber?? && obj.subNumber !=0>
  <a
    href="<#if type = "tag"><@spring.url "/tag/subscribers/${obj.tagId}"/><#else><@spring.url "/user/subscribers/${obj.accountId}"/></#if>"
    title="查看订阅&quot;${obj.name}&quot;的用户">
  ${obj.subNumber}
  </a>
  <#else>
  0
  </#if>
  <#if extText><span> 人订阅</span></#if>
</#macro>

<#macro messageNumber obj type extText=true>
  <#if obj.msgNumber?? && obj.msgNumber !=0>
  <a href="<@spring.url "/message/list/"/><#if type = "tag">tag/${obj.tagId}<#else>merchant/${obj.accountId}</#if>"
     title="查看&quot;${obj.name}&quot;的消息">
  ${obj.msgNumber}
  </a>
  <#else>
  0
  </#if>
  <#if extText><span> 条消息</span></#if>
</#macro>

<#macro userNumber tag extText=true>
  <#if tag.userNumber?? && tag.userNumber != 0>
  <a href="<@spring.url "/user/list/tag/${tag.tagId}"/>" title="查看&quot;${tag.name}&quot;相关商家">
  ${tag.userNumber}
  </a>
  <#else>
  0
  </#if>
  <#if extText><span> 个商家</span></#if>
</#macro>

<#macro locationLink location default>
  <#if location??>
  <a href="" title="查看地图">${location.longitude}-${location.latitude}</a>
  <#else>
  ${default}
  </#if>
</#macro>

<#macro subscribe subscribed func name class>
  <#if subscribed>
  <#-- already subscribed -->
    <#assign img="subscribed"/>
    <#assign title="取消订阅&quot;${name}&quot;"/>
  <#else>
    <#assign img="unsubscribed"/>
    <#assign title="订阅&quot;${name}&quot;"/>
  </#if>
<button type="button" title="${title}" class="${class}" onclick="${func}">
  <img src="<@spring.url "/static/images/lps/${img}.png"/>"/>
</button>
</#macro>
