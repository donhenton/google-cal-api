<#include "../common/header.ftl">
         
        
 <div class="well"> 
     
     
     <#if loggedIn??>
     <h4>You are logged in.</h4>
     <ul>
     <li><b>Oauth Info:</b> ${oauthInfo}</li>
     <li><b>call back code:</b> ${codeInfo}</li>
     
     </ul>
     <#else>
     <h3>Login By Clicking on the login link</h3>
     </#if>
     </div>
<#include "../common/footer.ftl">