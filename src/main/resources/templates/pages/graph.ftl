<!DOCTYPE HTML>
<html>

    <head>
        <title>${appTitle}</title>

        <link href="webjars/jsapp/foundation-icons.css"   type="text/css" rel="stylesheet"  />  
        <link href="webjars/jsapp/jquery-ui-theme.css" rel="stylesheet" type="text/css"/>  
        <link href="css/app_code.css" rel="stylesheet" type="text/css"/>
        
        <script src="/webjars/jquery/2.2.1/dist/jquery.min.js"  type="text/javascript"></script>  
        <script src="/webjars/jquery-ui/1.11.4/jquery-ui.min.js"  type="text/javascript"></script>  
        <script src="/js/ui-init.js" type="text/javascript"></script>   
        <script src="/js/code/bundle.js" type="text/javascript"></script>
        <link href="webjars/jsapp/main_app_style.css" rel="stylesheet" type="text/css"/>
        </head>
    <body>

        <section id="pageContainer">
            <header>
                <figure class="logo">${appTitle}</figure>
                 
                </header>
            <section id="main" class="grouping">
                <div class="mainPageContainer">
                    <!--begin content-->


         
        
 <div class="well"> 
    
     <div class="row">
        <table border="1" class="table">
            <tr>
                <td><div id="horizontalMain"></div></td>
                <td><button id="updateHorizontalData" class="pull-right btn btn-primary">Update Data</button></td>
            </tr>
        </table>
    </div>
     
     
 </div>    
<#include "../common/footer.ftl">