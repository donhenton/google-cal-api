<#include "../../common/header.ftl">

<style>
    

    #text
    {
        width: 350px;
        height: 100px;

    }


    </style>
<div class="column50Left"> 




    <form  method="POST" action="/mainDemoPost" class="form well">


        <table class="table" >
               <tr>
               <td><label for="dateString">Date For Event</label></td>
               <td> <input type="text" value="${mainModel.dateField}" size="12"
                        name="dateField" id="dateString"></td>
               </tr>
               <tr>
               <td><label for="firstAttendee">Attendee #1</label></td>
               <td> <input type="text" size="30"  value="${mainModel.firstAttendee}"
                        name="firstAttendee" id="firstAttendee"></td>
               </tr>
                <tr>
               <td><label for="secondAttendee">Attendee #2</label></td>
               <td> <input type="text" size="30" value="${mainModel.secondAttendee}"
                        name="secondAttendee" id="secondAttendee"></td>
               </tr>
                 <tr>
               <td><label for="fileName">File Name</label></td>
               <td> <input type="text" size="30" value="${mainModel.fileName}"
                        name="fileName" id="fileName"></td>
               </tr>
               <tr>
               <td><label for="linkValue">Link Value</label></td>
               <td> <input type="text" size="30"
                        name="linkValue" value="${mainModel.linkValue}" id="linkValue"></td>
               </tr>
                <tr>
               <td><label for="linkText">Link Text/label></td>
               <td> <input type="text" size="30"
                        name="linkText" value="${mainModel.linkText}" id="linkText"></td>
               </tr>
                <tr>
               <td><label for="linkText">Text</td>
               <td>
                   <textarea id="text" name="text">${mainModel.text}</textarea>    
                   
               </tr>
               
            <tr><td colspan="2">
                    <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
                    <input class="btn btn-large btn-primary" value="Input Submit" type="submit">
                    </td></tr>
            </table>








        </form>

    </div>


<#include "../../common/footer.ftl">