<#macro page>
<div class="ly-footer">
  <div class="wrapper">
    <div class="left">
      <span><@spring.message "meta.rights"/></span>
      |
      <a href="<@spring.url "/about"/>"><@spring.message "page.footer.about"/></a>
      |
      <a href="mailto:<@spring.message "meta.email"/>"><@spring.message "page.footer.contact"/></a>
    </div>
    <div class="left">
      <#nested/>
    </div>
    <div class="clear"></div>
  </div>
</div>
</#macro>
