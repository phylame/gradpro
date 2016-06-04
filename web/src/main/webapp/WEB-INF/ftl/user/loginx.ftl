<#import "widgets/html.ftl" as html/>
<@html.html5>
  <@html.head pageTitle!"登录">
  <style type="text/css">
    #login_div {
      width: 400px;
      margin: 0 auto;
      font-size: 16px;
    }

    #login_div .field {
      width: 80%;
      margin: 4px auto;
    }

    #login_div .text {
      font-size: 16px;
      padding: 8px 8px;
      border-radius: 4px;
      border: 1px solid #008B8B;
    }

    #login_error_field {
      color: red;
      padding: 0;
      font-size: 14px;
      font-weight: bold;
      text-align: left;
    }

    #login_two_fields {
      padding: 0;
    }

    #login_keep_me {
      float: left;
    }

    #login_reset_pwd {
      float: right;
    }

    #login_submit_btn {
      padding: 8px 0;
      color: white;
      background: #008B8B;
      font-size: 16px;
      border: 0;
      border-radius: 4px;
      cursor: pointer;
    }
  </style>
  <script>
    function validate_form(form) {
      with (form) {
        var tag = $("#login_error_field");
        if (!validateField(username, inRange(3, 25), "<fmt:message key="register.error.username
        "/>", tag
      ))
        {
          tag.css("display", "block");
          username.focus();
          return false;
        }
        if (!validateField(password, moreThan(5, 17), "<fmt:message key="register.error.password
        "/>", tag
      ))
        {
          tag.css("display", "block");
          password.focus();
          return false;
        }
        tag.css("display", "none")
        return true;
      }
    }
  </script>
  </@html.head>
  <@html.body bannerTitle!pageTitle!"登录">
  <form action="" method="post" id="login_div" class="ly-form" onsubmit="return validate_form(this)">
    <@spring.formHiddenInput "loginForm.type"/>
    <@spring.message "user.login.username" _login_username />
    <@spring.formInput "loginForm.username" "placeholder='${_login_username}' cssClass='text field'"/>
    <@spring.message "user.login.password" _login_password />
    <@spring.formPasswordInput "loginForm.password" "placeholder='${_login_password }' cssClass='text field'" />
    <#if errorText??>

      <div id="login_error_field" class="field">
      ${errorText}
      </div>
    <#else>
      <div id="login_error_field" class="field" style="display: none;"></div>
    </#if>
    <div id="login_two_fields" class="field">
      <div id="login_keep_me">
        <@spring.message "user.login.remember" _login_remember />
              <@form.formCheckboxes "loginForm.remember" "" "/" "cssClass='roundedTwo' label='${_login_remember}'" />
      </div>
      <div id="login_reset_pwd">
        <a href="<@spring.url "/user/reset"/> "><@spring.message "user.login.forget" /></a>
      </div>
      <div class="clear"></div>
    </div>
    <button id="login_submit_btn" type="submit" class="field">
      <@spring.message "user.login.submit" />
    </button>
  </form>
  </@html.body>
</@html.html5>
