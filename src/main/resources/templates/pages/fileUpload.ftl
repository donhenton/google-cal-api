<#include "../common/header.ftl">
<style>
    #result
    {
        width: 800px;
        height: 300px;

    }

    </style>

<div> 
    <p>
        <form method="GET" action="/fileUpload"><input type="submit" value="Submit" class="btn btn-primary" /></form>
    </p>
<h3>Result</h3>
<p>&nbsp;</p>
<textarea id="result">${result}</textarea>
</div>



<#include "../common/footer.ftl">