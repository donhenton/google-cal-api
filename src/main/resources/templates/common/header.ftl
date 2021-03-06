<!DOCTYPE HTML>
<html>

    <head>
        <title>${appTitle}</title>

        <link href="webjars/jsapp/foundation-icons.css"   type="text/css" rel="stylesheet"  />  
        <link href="webjars/jsapp/jquery-ui-theme.css" rel="stylesheet" type="text/css"/>  
        <link href="css/app_code.css" rel="stylesheet" type="text/css"/>
        
         <#if basePath?has_content>
                    <script> var basePath = '${basePath}';
                        var csrfToken =  "${_csrf.token}";
                    </script>
                    
        </#if>
        <#if !basePath?has_content>
                    <script> var basePath = null;</script>
        </#if>
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
                <nav class="topMenu grouping">


                    <ul>
                        <li><a href="/powerPointGraph">PowerPoint Demo</a></li>
                        <li><a href="/calendarDemo">Calendar Demo</a></li>
                        <li>

                            <aside>File API</aside>
                            <ul>
                                <li><a href="/fileList">Listing</a></li>
                                <li><a href="/fileUploadPage">Upload</a></li>
                                </ul>
                            </li>

                        <li><a href="/logout">Logout</a></li>
                        <li><a href="/">Home</a></li>
                        </ul>    


                    </nav>
                </header>
            <section id="main" class="grouping">
                <div class="mainPageContainer">
                    <!--begin content-->

