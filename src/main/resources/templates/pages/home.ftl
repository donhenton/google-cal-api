<#include "../common/header.ftl">
         
        
 <div class="well"> 
     
     
     <#if loggedIn??>
     <h4>You are logged in.</h4>
     <p>${oauthInfo}</p>
     <#else>
     <h3>Login By Clicking on the login link</h3>
     </#if>
     </div>
<#include "../common/footer.ftl">