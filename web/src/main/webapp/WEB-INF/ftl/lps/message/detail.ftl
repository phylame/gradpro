<#import "widgets/html.ftl" as html/>
<#import "widgets/items.ftl" as items/>
<@html.html pageTitle "message.details.title"; title>
  <@html.head title>
    <@html.lyCss "table"/>
    <@html.lyJs "ly-lps"/>
  <script>
    function delete_message() {
      deleteMessage("${message.messageId}", function () {
        location.href = "<@spring.url "/message/list"/>";
      });
    }
  </script>
  </@html.head>
  <@html.body title>
    <@html.menu>
    <li><a href="<@spring.url "/message/list"/>" title="查看全部消息">全部消息</a></li>
    </@html.menu>
    <@html.content>
    <table id="h-table" style="margin:0;">
      <tr>
        <th><@spring.message "message.field.title" />:</th>
        <td>${message.title}</td>
      </tr>
      <tr>
        <th><@spring.message "message.field.vendor" />:</th>
        <td><@items.userLink message.vendor/></td>
      </tr>
      <tr>
        <th><@spring.message "message.field.tag" />:</th>
        <td><@items.tagLink message.tag/></td>
      </tr>
      <tr>
        <th><@spring.message "message.field.startTime" />:</th>
        <td>${message.startTime?datetime?string.long_short}</td>
      </tr>
      <tr>
        <th><@spring.message "message.field.endTime" />:</th>
        <td>${message.endTime?datetime?string.long_short}</td>
      </tr>
      <tr>
        <th><@spring.message "message.field.content" />:</th>
        <td>${message.content}</td>
      </tr>
    </table>
      <@html.ifCurrentUser message.vendor>
      <hr/>
        <@html.button "delete_message()" "删除此消息">删 除</@html.button>
      </@html.ifCurrentUser>
    </@html.content>
    <@html.clear/>
  </@html.body>
</@html.html>
