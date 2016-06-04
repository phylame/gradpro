<#import "widgets/html.ftl" as html/>
<#import "widgets/form.ftl" as form/>
<#import "widgets/items.ftl" as items/>
<@html.html pageTitle "app.new.title"; title>
  <@html.head title>
    <@html.lyCss "table"/>
  </@html.head>
  <@html.body title>
  <form action="<@spring.url "/ocs/app/new"/>" method="POST">
    <@spring.formHiddenInput "appForm.type"/>
    <table id="h-table">
      <tr>
        <@form.textRow "app.field.name" "appForm.name"/>
      </tr>
      <tr>
        <th><@spring.message "app.field.vendor"/>:</th>
        <td colspan="2"><@items.userLink currentUser/></td>
      </tr>
      <tr>
        <th><@spring.message "app.field.createTime"/>:</th>
        <td>${appForm.createTime?datetime?string.long_short}</td>
      </tr>
      <tr>
        <th><@spring.message "app.field.platform"/>:</th>
        <td>
          <select name="platform" class="select">
            <#list platforms?keys as key>
              <option value="${key}"/>${platforms[key]}
            </#list>
          </select>
        </td>
      </tr>
      <tr>
        <@form.longTextRow "app.field.description" "appForm.description"/>
      </tr>
      <@html.forError; msg>
        <tr>
          <th></th>
          <td colspan="2"><@form.error msg/></td>
        </tr>
      </@html.forError>
      <tr>
        <th></th>
        <td colspan="2"><@form.submitReset "app.new.submit"/></td>
      </tr>
    </table>
  </form>
  </@html.body>
</@html.html>
