/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.controllers;

import com.dhenton9000.google.cal.api.ApptModel;
import com.dhenton9000.google.drive.FileReturnInfo;
import com.dhenton9000.google.drive.GoogleDriveWriter;
import com.dhenton9000.google.drive.TemplateGenerator;
import com.dhenton9000.google.rest.utils.RestUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dhenton
 */
@Controller
public class FileListController {

    @Autowired
    OAuth2RestTemplate oAuth2RestTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(FileListController.class);
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final static String FIELD_ITEMS = "files(id,appProperties,createdTime,description,name,properties,webContentLink,webViewLink,fullFileExtension,mimeType)";
    private  static final String TEMPLATE_PATH = "/docx_templates/demo_template.docx";

    
    @RequestMapping("/fileList")
    public ModelAndView fileList(ModelAndView model) {

        URI url = null;
        String res = "didnt work";
        String filesOnlyQuery = "(not mimeType contains 'folder') and trashed = false ";
        String uriString = "https://www.googleapis.com/drive/v3";
        
         
        
        try {
            uriString = uriString + "/files?fields=" + URLEncoder.encode(FIELD_ITEMS, "UTF-8");
            uriString = uriString + "&q=" + URLEncoder.encode(filesOnlyQuery, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LOG.error("Encoding problem " + ex.getMessage());
        }
        List<File> fileItems = new ArrayList<File>();

        try {
            url = new URI(uriString);
        } catch (URISyntaxException ex) {
            LOG.error("could not create uri " + uriString);
            res = "could not create uri " + uriString;
        }
        if (url != null) {

            try {

                FileList fileList;

                ResponseEntity<String> responseOut = oAuth2RestTemplate.getForEntity(url, String.class);
                // LOG.info("a2");
                if (RestUtil.isError(responseOut.getStatusCode())) {

                    LOG.error("res is " + res+" "+responseOut.getBody());
                } else {
                    String dataIn = responseOut.getBody();
                    fileList = JSON_FACTORY.fromString(dataIn, FileList.class);
                    fileItems = new ArrayList<File>(fileList.getFiles());

                }

            } catch (IOException | RestClientException iex) {

                res = "cannot do file list   problem " + iex.getMessage() + " " + iex.getClass().getName();
                LOG.error(res);
            }

        } else {
            LOG.error("url null");
        }

        model.addObject("result", res);
        model.addObject("files", fileItems);
        model.addObject("appTitle", "Google File API");
        model.setViewName("pages/fileList");
        return model;

    }

    /**
     * menu navigation to the upload page
     *
     * @param model
     * @return
     */
    @RequestMapping("/fileUploadPage")
    public ModelAndView fileUpLoadPage(ModelAndView model) {
        String res = "";
        model.addObject("result", res);

        model.addObject("appTitle", "Google File Upload");
        model.setViewName("pages/fileUpload");
        return model;
    }

  
    @RequestMapping(path = "/fileUpload", method = RequestMethod.POST)
    public ModelAndView fileUpLoad(@ModelAttribute("apptModel") ApptModel apptModel, BindingResult result, ModelAndView model) {

        //http://stackoverflow.com/questions/21102071/resttemplate-upload-image-file
        String res = "nothing happened";
        LOG.debug("apptModel " + apptModel);
        URI url = null;

        String uriString = "https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart";

        try {
            url = new URI(uriString);
        } catch (URISyntaxException ex) {
            LOG.error("could not create uri " + uriString);
            res = "could not create uri " + uriString;
        }
        if (url != null) {
            try {
                GoogleDriveWriter writer = new GoogleDriveWriter();
                TemplateGenerator gen = new TemplateGenerator();
                Properties replacementProps = new Properties();
                LocalDate ld = LocalDate.now();
                String dateField = ld.format(DateTimeFormatter.ISO_LOCAL_DATE);
                
                replacementProps.setProperty("TO_FIELD", apptModel.getToField());
                replacementProps.setProperty("FROM_FIELD", apptModel.getFromField());
                replacementProps.setProperty("DATE_FIELD", dateField);
                replacementProps.setProperty("TEXT_FIELD", apptModel.getTextField());
                replacementProps.setProperty("HYPERLINK_HREF", "http://www.yahoo.com");
                replacementProps.setProperty("HYPERLINK_TEXT", "My cool link");
                String fileName = apptModel.getFileName();

                byte[] byteData
                        = gen.replaceToByteArray(replacementProps, TEMPLATE_PATH);
                String jsonMetaData = "{name: \""+fileName+".docx\"}";

                HttpEntity<MultiValueMap<String, Object>> infoEntity
                        = writer.produceEntity(byteData, jsonMetaData, GoogleDriveWriter.DOCX_MEDIA_TYPE);

                ResponseEntity<String> responseOut
                        = oAuth2RestTemplate.exchange(url,
                                HttpMethod.POST, infoEntity, String.class);
                res = responseOut.getBody();
                if (RestUtil.isError(responseOut.getStatusCode())) {
                    LOG.error("res is " + res);
                }
                else
                {
                   //TODO: write the calendar item,
                   //obtain the file id to use as an attachment
                    
                    FileReturnInfo fileResult =  JSON_FACTORY.fromString(res, FileReturnInfo.class);
                    String urlForFile = "https://www.googleapis.com/drive/v3/files/"+fileResult.getId() +"?fields=id,createdTime,webViewLink"  ;
                    ResponseEntity<JsonNode> responseFile = oAuth2RestTemplate.getForEntity(urlForFile, JsonNode.class);
                    JsonNode bodyObj = responseFile.getBody();
                    LOG.debug("zzz "+bodyObj.get("webViewLink"));
                    
                    
                }

            } catch (Exception iex) {

                res = "cannot do file upload  problem " + iex.getMessage();
                LOG.error(res);
            }

        } else {
            res = "Url could not be parsed";
        }

        model.addObject("result", res);
        model.addObject("apptModel", apptModel);
        model.addObject("appTitle", "Google File Upload");
        model.setViewName("pages/fileUpload");
        return model;
    }

}
