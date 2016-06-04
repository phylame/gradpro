<#macro tip code>
<th><#if code??><@spring.message code/>:</#if></th>
</#macro>

<#macro fieldError>
<td><@spring.showErrors "" "error"/></td>
</#macro>

<#macro text path attrs="" withError=true>
<td><@spring.formInput path "class=\"text\" ${attrs}"/></td>
  <#if withError><@fieldError/></#if>
</#macro>

<#macro password path attrs="" withError=true>
<td><@spring.formPasswordInput path "class=\"text\" ${attrs}"/></td>
  <#if withError><@fieldError/></#if>
</#macro>

<#macro longText path attrs="" withError=true>
<td><@spring.formTextarea path "class=\"long-text\" ${attrs}"/></td>
  <#if withError><@fieldError/></#if>
</#macro>

<#macro textRow titleCode path attrs="" withError=true>
  <@tip titleCode/>
  <@form.text path attrs withError/>
</#macro>

<#macro passwordRow titleCode path attrs="" withError=true>
  <@tip titleCode/>
  <@form.password path attrs withError/>
</#macro>

<#macro longTextRow titleCode path attrs="" withError=true>
  <@tip titleCode/>
  <@form.longText path attrs withError/>
</#macro>

<#macro select path items>
  <@spring.formSingleSelect path items "class=\"select\""/>
</#macro>

<#macro error msg>
<span class="error">${msg}</span>
</#macro>

<#macro submit code>
<input type="submit" class="button" value="<@spring.message code/>"/>
</#macro>

<#macro reset>
<input type="reset" class="button" value="<@spring.message "common.button.reset"/>"/>
</#macro>

<#macro submitReset code>
  <@submit code/> <@reset/>
</#macro>
