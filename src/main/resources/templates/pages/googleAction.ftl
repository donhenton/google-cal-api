<#include "../common/header.ftl">
 <style>
     #result
     {
        width: 400px;
        height: 300px;
         
     }
     
 </style>
        
 <div class="column50Left"> 
     
     <h3>Result</h3>
     <p>&nbsp;</p>
     <textarea id="result">${result}</textarea>
</div>

 <div class="column50Right"> 
     
      <p>User ${userName} should have a new calendar entry for ${dateString}</p>
</div>


<#include "../common/footer.ftl">