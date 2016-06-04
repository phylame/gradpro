<#import "widgets/html.ftl" as html/>
<@html.html pageTitle "user.settings.title"; title>
  <@html.head title>
  </@html.head>
  <@html.body title>
  Your operations:
  <ul>
    <li><a href="<@spring.url "/user/reset"/>">Reset password</a></li>
    <li><a href="<@spring.url "/user/delete"/>">Delete account</a></li>
  </ul>
  </@html.body>
</@html.html>
