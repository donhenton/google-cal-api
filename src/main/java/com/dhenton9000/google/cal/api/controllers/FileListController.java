/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.controllers;

import com.dhenton9000.google.cal.api.image.ImageGenerator;
import com.dhenton9000.google.rest.utils.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    @RequestMapping("/fileList")
    public ModelAndView fileList(ModelAndView model) {

        URI url = null;
        String res = "didnt work";
        String filesOnlyQuery = "not mimeType contains 'folder'";
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

                //HttpHeaders headers = new HttpHeaders();
                //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                //headers.setContentType(MediaType.APPLICATION_JSON);
                FileList fileList;

                ResponseEntity<String> responseOut = oAuth2RestTemplate.getForEntity(url, String.class);
                // LOG.info("a2");
                if (RestUtil.isError(responseOut.getStatusCode())) {

                    LOG.error("res is " + res);
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

    @RequestMapping("/fileUpload")
    public ModelAndView fileUpLoad(ModelAndView model) {

        //http://stackoverflow.com/questions/21102071/resttemplate-upload-image-file
        ImageGenerator gen = new ImageGenerator();
        String res = "nothing happened";

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

                

                //the parts of the upload
                MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
                //the main header

                HttpHeaders mainHeaders = new HttpHeaders();
                mainHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
                
                //the meta data
                MultiValueMap<String, String> metaHeaders = new LinkedMultiValueMap<String, String>();
                metaHeaders.set("Content-type","application/json");
                String metaData = "{name: \"fred1000.png\"}";
                HttpEntity metaEntity = new HttpEntity(metaData,metaHeaders);
               
                //the image data
                MultiValueMap<String, String> imageHeaders = new LinkedMultiValueMap<String, String>();
                imageHeaders.set("Content-type","image/png");
                byte[] byteData = gen.createImage("png", "Get a job!!!!!!");
                Resource imageResource = new ByteArrayResource(byteData);
                HttpEntity imageEntity = new HttpEntity(imageResource,imageHeaders);
                 
                
                parts.add("metaData",metaEntity);
                parts.add("image", imageEntity);
                 

                HttpEntity<MultiValueMap<String, Object>> infoEntity = new HttpEntity<MultiValueMap<String, Object>>(parts, mainHeaders);
                ResponseEntity<String> responseOut
                        = oAuth2RestTemplate.exchange(url,
                                HttpMethod.POST, infoEntity, String.class);
                res = responseOut.getBody();
                if (RestUtil.isError(responseOut.getStatusCode())) {
                    LOG.error("res is " + res);
                }
            } catch (Exception iex) {

                res = "cannot do calendar list io problem " + iex.getMessage();
                LOG.error(res);
            }

        } else {
            res = "Url could not be parsed";
        }

        model.addObject("result", res);

        model.addObject("appTitle", "Google File Upload");
        model.setViewName("pages/fileUpload");
        return model;
    }
    /*
     
     
    @RequestMapping("/fileUpload")
    public ModelAndView fileUpLoad(ModelAndView model) {

        //http://stackoverflow.com/questions/21102071/resttemplate-upload-image-file
        ImageGenerator gen = new ImageGenerator();
        String res = "nothing happened";

        URI url = null;

        String uriString = "https://www.googleapis.com/upload/drive/v3/files?uploadType=media";

        try {
            url = new URI(uriString);
        } catch (URISyntaxException ex) {
            LOG.error("could not create uri " + uriString);
            res = "could not create uri " + uriString;
        }
        if (url != null) {
            try {

                byte[] byteData = gen.createImage("png", "Get a job!!!!!!");
                Resource resource = new ByteArrayResource(byteData);

                HttpHeaders headers = new HttpHeaders();

                headers.setContentType(MediaType.IMAGE_PNG);
                headers.setContentLength(byteData.length);

                HttpEntity<Resource> infoEntity = new HttpEntity<Resource>(resource, headers);
                ResponseEntity<String> responseOut
                        = oAuth2RestTemplate.exchange(url,
                                HttpMethod.POST, infoEntity, String.class);
                res = responseOut.getBody();
                if (RestUtil.isError(responseOut.getStatusCode())) {
                    LOG.error("res is " + res);
                }
            } catch (Exception iex) {

                res = "cannot do calendar list io problem " + iex.getMessage();
                LOG.error(res);
            }

        } else {
            res = "Url could not be parsed";
        }

        model.addObject("result", res);

        model.addObject("appTitle", "Google File Upload");
        model.setViewName("pages/fileUpload");
        return model;
    }
     */
}
