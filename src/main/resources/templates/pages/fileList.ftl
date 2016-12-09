<#include "../common/header.ftl">
 <div> 
     
     <h3>Result</h3>
     <p>&nbsp;</p>
     <table class="table table-striped">
       <tr><th>File Names</th><th>Link To File</th><th>Id</th></tr>
      <#list files as file>
         <tr><td>${file.name}</td><td><a href="${file.webViewLink}" target="_blank">Link</a></td><td>${file.id}</td></tr>
         
      </#list>
         
     </table>
     
</div>

<#include "../common/footer.ftl">