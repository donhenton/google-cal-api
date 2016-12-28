<#include "../../common/header.ftl">


<script src="/js/code/pptDownload.js" type="text/javascript"></script>       
<div class="well"> 

    <div class="row">
        <table border="1" class="table">
            <tr>
                <td><div id="horizontalMain"></div></td>
                <td><button id="updateHorizontalData" class="pull-right btn btn-primary">Update Data</button> 

                </tr>
            </table>
        </div>

    <div class="row">

        <form id="pptxForm" class="form inline-form">
            <label for="imageTitle">Title</label>
            <input type="text" size="35" id="imageTitle" name="imageTitle" value="Sample Title"></input>
            <label for="imageSubTitle">Sub Title</label>
            <input type="text" size="35" id="imageSubTitle" value="Sample Sub Title" name="imageSubTitle"></input>
            <span id="loader" class="loading"></span>
            <button type="submit" id="downloadPowerPoint" class="btn btn-primary">Download PPT</button></td>
        </form>

    </div>


</div>    
<#include "../../common/footer.ftl">