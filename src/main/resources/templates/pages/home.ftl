<#include "../common/header.ftl">
         
        
 <div class="well"> 
     
     <p>${totalUrl}</p>
      
     <form  method="POST" action="/googleAction" class="form inline-form">
         <label for="information">Date For Event</label>
          <input type="text" value="${initialDate}" name="dateString" id="dateString">
         <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
         <input class="btn btn-large btn-primary" value="Input Submit" type="submit">
     </form>
      
     </div>
<#include "../common/footer.ftl">