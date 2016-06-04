<#import "widgets/util.ftl" as util/>
<#import "widgets/html.ftl" as html/>
<@html.html pageTitle "user.login.title"; title>
<@html.head title>
<style>
#login-form {
  width: 400px;
  margin: 0 auto;
}

#login-form .field {
  margin: 4px 0 auto;
  display: block;
  font-size: 1em;
}

#login-form .input {
  width: 340px;
  padding: 8px 4px;
  border: 1px solid #008B8B;
  border-radius: 4px;
}

#error-span {
  padding: 0;
  color: red;
  font-weight: bold;
  text-align: left;
}

#submit-button {
  width: 348px;
  padding: 8px 4px;
  border: 0;
  border-radius: 4px;
  cursor: pointer;
  color: white;
  background: #008B8B;
}
</style>
<script>
function validate_form(form) {
  with (form) {
    var tag = $("#error-span");
    if (!validateField(username, inRange(3, 25), null, null)) {
      tag.css("display", "block");
      tag.text("<@spring.message "user.register.error.username"/>");
      username.focus();
      return false;
    }
    if (!validateField(password, moreThan(5, 17), null, null)) {
      tag.css("display", "block");
      tag.text("<@spring.message "user.register.error.password"/>");
      password.focus();
      return false;
    }
    tag.css("display", "none")
    return true;
  }
}
</script>
</@html.head>
<@html.body title>
  <form action="<@spring.url "/user/login"/>" method="post" id="login-form" onsubmit="return validate_form(this)">
    <@spring.formHiddenInput "loginForm.type"/>
    <@util.message "user.login.username"; text>
      <@spring.formInput "loginForm.username" "placeholder=\"${text}\" class=\"input field\""/>
    </@util.message>
    <@util.message "user.login.password"; text>
      <@spring.formPasswordInput "loginForm.password" "placeholder=\"${text}\" class=\"input field\""/>
    </@util.message>
    <#if errorText??>
      <span id="error-span" class="field">${errorText}</span>
    <#else>
      <span id="error-span" class="field" style="display:none"></span>
    </#if>
    <div style="padding:0 4px;width:340px;" class="field">
      <label for="remember"><@spring.message "user.login.remember"/></label>
      <@spring.formCheckbox "loginForm.remember" "class=\"left\""/>
      <a href="<@spring.url "/user/reset"/>" class="right"><@spring.message "user.login.forget"/></a>
      <@html.clear/>
    </div>
    <button id="submit-button" type="submit" class="field"><@spring.message "user.login.submit"/></button>
  </form>
</@html.body>
</@html.html>