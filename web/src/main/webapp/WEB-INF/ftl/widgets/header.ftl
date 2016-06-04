<#macro page bannerTitle>
<script type="text/javascript">
  function validateSearch(form) {
    with (form) {
      return validateField(q, notEmpty, "<@spring.message "page.header.search.noInput"/>", function (field, error) {
        alert(error);
        field.focus();
        return false;
      });
    }
  }
</script>
<div class="top-bar">
  <div class="wrapper">
    <div class="menu-bar left">
      <ul>
        <li>
          <a href="<@spring.url "/"/>"
             title="<@spring.message "page.header.home.tip"/>"><@spring.message "page.header.home"/></a>
        </li>
        <li>
          <a href="<@spring.url "/publish"/>"
             title="<@spring.message "page.header.pub.tip"/>"><@spring.message "page.header.pub"/></a>
        </li>
        <li>
          <a href="<@spring.url "/subscribe"/>"
             title="<@spring.message "page.header.sub.tip"/>"><@spring.message "page.header.sub"/></a>
        </li>
        <li>
          <a href="<@spring.url "/tag/list"/>"
             title="<@spring.message "page.header.tag.tip"/>"><@spring.message "page.header.tag"/></a>
        </li>
        <li>
          <a href="<@spring.url "/message/list"/>"
             title="<@spring.message "page.header.message.tip"/>"><@spring.message "page.header.message"/></a>
        </li>
        <li>
          <a href="<@spring.url "/ocs"/>"
             title="<@spring.message "page.header.ocs.tip"/>"><@spring.message "page.header.ocs"/></a>
        </li>
      </ul>
    </div>
    <div class="user-bar right">
      <@spring.message "page.header.who"/>
      <#if !currentUser??>
        <@spring.message "page.header.guest"/>
        [
        <a href="<@spring.url "/user/login"/>"><@spring.message "page.header.login"/></a>
        |
        <a href="<@spring.url "/user/register"/>"><@spring.message "page.header.join"/></a> ]
      <#else>
        <a href="<@spring.url "/user/detail/${currentUser.accountId}"/>"
           title="<@spring.message "page.header.details.tip"/>">
        ${currentUser.name}
        </a>
        |
        <a href="<@spring.url "/user/logout?form=${currentURL}"/>" title="<@spring.message "page.header.logout.tip"/>">
          <@spring.message "page.header.logout"/>
        </a>
      </#if>
    </div>
    <div class="clear"></div>
  </div>
</div>
<div class="banner">
  <div class="wrapper">
    <span class="left">${bannerTitle!"(Unknown Page)"}</span>
    <form action="<@spring.url "/search"/>" method="get" onsubmit="return validateSearch(this)" class="right">
      <select name="scope" class="select left">
        <#-- <option id="opt_all" value="all">全部</option> -->
        <option id="opt_tag" value="tag">商品</option>
        <option id="opt_user" value="user">商家</option>
      </select>
      <input type="text" id="txt_q" name="q" placeholder="<@spring.message "page.header.search.tip"/>"
             class="text left"/>
      <input type="submit" value="<@spring.message "page.header.search.button"/>" class="button left"/>
    </form>
    <div class="clear"></div>
  </div>
</div>
</#macro>
