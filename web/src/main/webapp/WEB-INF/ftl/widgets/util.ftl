<#-- get localized message with arguments -->
<#macro tr code args...>
  <@spring.messageArgs code args/>
</#macro>

<#-- get value or from resource bundle by key -->
<#macro text value key>
  <#if value??>
  ${value}
  <#else>
    <@spring.message key/>
  </#if>
</#macro>

<#macro message code>
<#local _text>
<@spring.message code/>
</#local>
<#nested _text/>
</#macro>