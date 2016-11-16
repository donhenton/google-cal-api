<#include "../common/header.ftl">
         
        
 <div class="well"> 
     
     
      
     <form  method="POST" action="/googleAction" class="form inline-form">
         <label for="information">Information</label>
         <input type="text" name="information" size="35" id="information">
         <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
         <input class="btn btn-large btn-primary" value="Input Submit" type="submit">
     </form>
      
     </div>
<#include "../common/footer.ftl">