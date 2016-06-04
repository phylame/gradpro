<#import "widgets/html.ftl" as html/>
<@html.html pageTitle "user.reset.title"; title>
  <@html.head title>
    <@html.lyJs "ly-ocs"/>
  <script>
    function goto_pwd() {
      var username = $("#un").val();
      if (!username) {
        alert("请输入用户名");
        $("#un").focus();
        return;
      }
      if (!isAvailableAccount(username)) {
        alert("无效用户名");
        $("#un").focus();
        return;
      }
      $("#con").html('\
    <form action="<@spring.url "/user/reset"/>" method="post">\
    <label for="password">新密码:</label>\
    <input type="hidden" name="username" value="{0}"/>\
    <input type="password" id="password" name="password" class="text">\
    <label for="confirm">确认:</label>\
    <input type="password" id="confirm" name="confirm" class="text">\
    <input type="submit" class="button" value="确 定"/>\
    </form>'.format(username));
    }
  </script>
  </@html.head>
  <@html.body title>
  <div id="con">
    Your username: <input type="text" id="un" class="text" value="${currentUser.name}"/> <@html.button "goto_pwd()">
    Next</@html.button>
  </div>
  </@html.body>
</@html.html>
