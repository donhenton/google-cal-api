


$(document).ready(
        function () {


            var frm = $('#pptxForm');
            frm.submit(function (ev) {
                 doPPTDownload();
                ev.preventDefault();
            });




        });


/* global basePath, csrfToken */
/* defined on page via java */

function doPPTDownload()
{
    //basePath is global via freemaker and the controller see PowerPointController

    var fullPath = basePath + "/pptDownload";
    var xhr = new XMLHttpRequest();
    xhr.open('POST', fullPath, true); //to the download servlet

    var dataObj = {};
    dataObj['svgData'] = $('svg').parent().html();
    dataObj['imageTitle'] = $('#imageTitle').val();
    dataObj['imageSubTitle'] = $('#imageSubTitle').val();
    var dataToSend = JSON.stringify(dataObj);
    console.log(dataToSend);
    
    $('#loader').css({"visibility": "visible"});
    xhr.responseType = 'arraybuffer';
    xhr.addEventListener("loadend", function ()
    {
        $('#loader').css({"visibility": "hidden"})
    })
    xhr.addEventListener("onerror", function (event)
    {
        window.alert(JSON.stringify(event));
    })
    xhr.onerror = function (ev)
    {
        window.alert(JSON.stringify(ev));
    }
    xhr.onload = function () {
        if (this.status === 200) {
            var filename = "";
            var disposition = xhr.getResponseHeader('Content-Disposition');
            if (disposition && disposition.indexOf('attachment') !== -1) {
                var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                var matches = filenameRegex.exec(disposition);
                if (matches != null && matches[1])
                    filename = matches[1].replace(/['"]/g, '');
            }
            var type = xhr.getResponseHeader('Content-Type');
            var a = null;
            var blob = new Blob([this.response], {type: type});
            if (typeof window.navigator.msSaveBlob !== 'undefined') {
                // IE workaround for "HTML7007: One or more blob URLs were revoked by closing the blob for which they were created. These URLs will no longer resolve as the data backing the URL has been freed."
                window.navigator.msSaveBlob(blob, filename);
            } else {
                var URL = window.URL || window.webkitURL;
                var downloadUrl = URL.createObjectURL(blob);

                if (filename) {
                    // use HTML5 a[download] attribute to specify filename
                    a = document.createElement("a");
                    a.setAttribute("id", "downLoadLink")
                    // safari doesn't support this yet
                    if (typeof a.download === 'undefined') {
                        window.location = downloadUrl;
                    } else {
                        a.href = downloadUrl;
                        a.download = filename;
                        document.body.appendChild(a);
                        a.click();
                    }
                } else {
                    window.location = downloadUrl;
                }

                setTimeout(function () {
                    URL.revokeObjectURL(downloadUrl);
                    $('a#downLoadLink').remove();
                    console.log("timeout clean up called");
                }, 100); // cleanup
            }
        } else
        {
            window.alert(" error status " + this.status);
        }
    };
     xhr.setRequestHeader('Content-Type', 'application/json');
   // xhr.setRequestHeader('Content-Type', 'image/svg+xml');
    xhr.setRequestHeader("X-XSRF-TOKEN", csrfToken);
    xhr.send(dataToSend);

//application/vnd.openxmlformats-officedocument.presentationml.presentation


}