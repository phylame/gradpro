<#import "widgets/html.ftl" as html/>
<@html.html pageTitle "ocs.api.title"; title>
  <@html.head title>
    <@html.lyCss "table"/>
  </@html.head>
  <@html.body title>
  <table id="v-table">
    <thead>
    <tr>
      <th>URL</th>
      <th>方法</th>
      <th>参数</th>
      <th>描述</th>
    </tr>
    </thead>
    <tr>
      <td><@spring.url "/user/login/{clientId}"/></td>
      <td>POST</td>
      <td>{username: "", password: ""}</td>
      <td>登陆系统</td>
    </tr>
    <tr class="alt">
      <td><@spring.url "/user/logout/{clientId}/{accountId}"/></td>
      <td>POST</td>
      <td>(None)</td>
      <td>退出系统</td>
    </tr>
    <tr>
      <td><@spring.url "/user/subscriptions/{clientId}/{accountId}"/></td>
      <td>POST</td>
      <td>(None)</td>
      <td>获得用户订阅</td>
    </tr>
    <tr class="alt">
      <td><@spring.url "/tmr/new/{clientId}/{accountId}"/></td>
      <td>POST</td>
      <td>{...}</td>
      <td>发布需求</td>
    </tr>
    <tr>
      <td><@spring.url "/tmr/discard/{clientId}/{accountId}"/></td>
      <td>POST</td>
      <td>{tmrID: ""}</td>
      <td>取消需求</td>
    </tr>
    <tr class="alt">
      <td><@spring.url "/tml/{clientId}/{accountId}"/></td>
      <td>POST</td>
      <td>{...}</td>
      <td>通知用户位置</td>
    </tr>
  </table>
  </@html.body>
</@html.html>
