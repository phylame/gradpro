<#macro page pager pageIndices>
<hr/>
<div class="ly-pager">
  <#assign currentPage = pager.currentPage />
  <#assign pageSize = pager.pageSize />
  <div class="left">
    <span>共 ${pager.totalElements} 个结果，当前： ${currentPage}/${pager.totalPages}</span>
    <#if pager.totalElements gt 2>
      <#if pager.totalElements gt 10>
        <#assign top=10/>
      <#else>
        <#assign top=pager.totalElements/>
      </#if>
      <span>，大小：
   <select style="height:18px"
           onchange="location.href='<@spring.url "${currentURL}?pageNumber=${currentPage}&pageSize="/>'+this.value">
     <#list 2..top as i>
       <option value="${i}" <#if i = pageSize>selected="selected"</#if>>${i}</option>
     </#list>
   </select>
   </span>
    </#if>
  </div>
  <div class="right">
    <ul>
      <#if !pager.first>
        <li><a href="${currentURL}?pageNumber=1&pageSize=${pageSize}">首页</a></li>
        <li><a href="${currentURL}?pageNumber=${currentPage-1}&pageSize=${pageSize}">上一页</a></li>
      </#if>
      <#list pageIndices as idx>
        <li>
          <#if idx != currentPage>
            <a href="${currentURL}?pageNumber=${idx}&pageSize=${pageSize}" class="normal">${idx}</a>
          <#else>
            <span class="select">${idx}</span>
          </#if>
        </li>
      </#list>
      <#if !pager.last>
        <li><a href="${currentURL}?pageNumber=${currentPage+1}&pageSize=${pageSize}">后一页</a></li>
        <li><a href="${currentURL}?pageNumber=${pager.totalPages}&pageSize=${pageSize}">末页</a></li>
      </#if>
    </ul>
  </div>
  <div class="clear"></div>
</div>
</#macro>
