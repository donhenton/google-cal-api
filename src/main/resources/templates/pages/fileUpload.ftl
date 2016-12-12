<#include "../common/header.ftl">
<#import "/spring.ftl" as spring />
<style>
    #result
    {
        width: 400px;
        height: 300px;

    }

    #textField
    {
        width: 350px;
        height: 200px;

    }


    </style>

<div> 



    <div class="column50Left">
        <form class="form well" method="POST" action="/fileUpload">
            <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>

            <table class="table ">
                <tbody>
                    <tr><th><label for="toField">To Email:</label></th><td>
                
                            <input type="text"  name="toField"  size="35" id="toField"></td></tr>
                    <tr><th><label for="fromField">From Email:</label></th><td>
             
                            <input type="text"  name="fromField"  size="35" id="fromField"></td></tr>
                    <tr><th><label for="fileName">FileName (no ext):</label></th><td>
             
                            <input type="text"  name="fileName"  size="35" id="fileName"></td></tr>


                    <tr><th><label for="textField">Text:</label></th>
                        <td>
                          
                            <textarea id="textField" name="textField" class="textField-class"></textarea>    
                            </td>
                        </tr>


                    <tr>


                        <td colspan="2">

                            <input class="btn btn-large btn-primary" value="Input Submit" type="submit"> 
                            </td>
                        </tr>





                    </tbody></table>


        </div>


    <div class="column50Right">
        <h3>Result</h3>
        <p>&nbsp;</p>
        <textarea id="result">${result}</textarea>
        </div>



<#include "../common/footer.ftl">