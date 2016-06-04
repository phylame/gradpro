<#import "widgets/html.ftl" as html/>
<#import "widgets/form.ftl" as form/>
<#import "widgets/items.ftl" as items/>
<@html.html pageTitle "tag.new.title"; title>
  <@html.head title>
    <@html.lyCss "table"/>
  </@html.head>
  <@html.body title>
  <form action="<@spring.url "/tag/new"/>" method="POST">
    <@spring.formHiddenInput "tagForm.type"/>
    <table id="h-table">
      <tr>
        <@form.textRow "common.field.name" "tagForm.name"/>
      </tr>
      <tr>
        <th><@spring.message "common.field.creator"/>:</th>
        <td colspan="2"><@items.userLink currentUser/></td>
      </tr>
      <tr>
        <@form.longTextRow "common.field.intro" "tagForm.intro"/>
      </tr>
      <@html.forError; msg>
        <tr>
          <th></th>
          <td colspan="2"><@form.error msg/></td>
        </tr>
      </@html.forError>
      <tr>
        <th></th>
        <td colspan="2"><@form.submitReset "tag.new.submit"/></td>
      </tr>
    </table>
  </form>
  </@html.body>
</@html.html>
