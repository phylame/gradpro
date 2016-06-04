<#import "widgets/html.ftl" as html/>
<#import "widgets/form.ftl" as form/>
<#import "widgets/items.ftl" as items/>
<@html.html pageTitle "pub.title"; title>
  <@html.head title>
    <@html.lyCss "table"/>
  </@html.head>
  <@html.body title>
  <form action="<@spring.url "/publish"/>" method="POST">
    <@spring.formHiddenInput "pubForm.type"/>
    <table id="h-table">
      <tr>
        <@form.textRow "message.field.title" "pubForm.title"/>
      </tr>
      <tr>
        <@form.tip "message.field.vendor"/>
        <td colspan="2"><@items.userLink currentUser/></td>
      </tr>
      <tr>
        <@form.tip "message.field.tag"/>
        <td colspan="2"><@form.select "pubForm.tag" tags/></td>
      </tr>
      <tr>
        <@form.textRow "message.field.startTime" "pubForm.startTime"/>
      </tr>
      <tr>
        <@form.textRow "message.field.endTime" "pubForm.endTime"/>
      </tr>
      <tr>
        <@form.longTextRow "message.field.content" "pubForm.content"/>
      </tr>
      <@html.forError; msg>
        <tr>
          <th></th>
          <td colspan="2"><@form.error msg/></td>
        </tr>
      </@html.forError>
      <tr>
        <th></th>
        <td colspan="2"><@form.submitReset "pub.form.submit"/></td>
      </tr>
    </table>
  </form>
  </@html.body>
</@html.html>
