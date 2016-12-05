<#include "../common/header.ftl">
         
        
 <div class="column50Left"> 
     
     <p>${totalUrl}</p>
      
     <form  method="POST" action="/googleAction" class="form inline-form">
         <label for="information">Date For Event</label>
          <input type="text" value="${initialDate}" name="dateString" id="dateString">
         <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
         <input class="btn btn-large btn-primary" value="Input Submit" type="submit">
     </form>
      
     </div>

<div class="column50Right">
    <h3>User Details</h3>
    
    <table class="table table-striped">
        <tr><td>Id</td><td>${userInfo.id}</td></tr>
        <tr><td>Name</td><td>${userInfo.name}</td></tr>
        <#if userInfo.link??>
        <tr><td>Link</td><td><a href="${userInfo.link}" _target="_new">Google Plus</a></td></tr>
        </#if>
        
    </table>
    
     
</div>
<#include "../common/footer.ftl">