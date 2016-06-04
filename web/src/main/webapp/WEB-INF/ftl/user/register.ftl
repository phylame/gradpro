<#import "widgets/html.ftl" as html/>
<#import "widgets/form.ftl" as form/>
<@html.html pageTitle "user.register.title"; title>
  <@html.head title>
    <@html.lyCss "table"/>
  </@html.head>
  <@html.body title>
  <form action="<@spring.url "/user/register"/>" method="post">
    <@spring.formHiddenInput "regForm.type"/>
    <table id="h-table">
      <tr>
        <@form.textRow "user.field.username" "regForm.username" "maxLength=\"24\""/>
      </tr>
      <tr>
        <@form.passwordRow "user.field.password" "regForm.password" "maxLength=\"16\""/>
      </tr>
      <tr>
        <@form.passwordRow "user.register.confirm" "regForm.confirm" "maxLength=\"16\""/>
      </tr>
      <tr>
        <@form.textRow "common.field.name" "regForm.name" "maxLength=\"32\""/>
      </tr>
      <tr>
        <@form.textRow "user.field.telephone" "regForm.telephone" "maxLength=\"20\""/>
      </tr>
      <tr>
        <@form.longTextRow "common.field.intro" "regForm.intro"/>
      </tr>
      <@html.forError; msg>
        <tr>
          <th></th>
          <td colspan="2"><@form.error msg/></td>
        </tr>
      </@html.forError>
      <tr>
        <th></th>
        <td colspan="2"><@form.submitReset "register.submit"/></td>
      </tr>
    </table>
  </form>
  </@html.body>
</@html.html>
