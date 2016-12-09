<#include "../common/header.ftl">
 <div class="column50Left"> 
     
     <h3>Result</h3>
     <p>&nbsp;</p>
     <table class="table table-striped">
       <tr><th>File Names</th></tr>
      <#list files as file>
         <tr><td>${file.name}</td></tr>
         
      </#list>
         
     </table>
     
</div>

<#include "../common/footer.ftl">