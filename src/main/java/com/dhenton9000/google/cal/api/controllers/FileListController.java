/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.controllers;

import com.dhenton9000.google.rest.utils.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
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
    private final static String FIELD_ITEMS =  "files(appProperties,createdTime,description,name,properties,webContentLink,webViewLink,fullFileExtension,mimeType)";
    @RequestMapping("/fileList")
    public ModelAndView fileList(ModelAndView model) {

        String urlBase = "https://www.googleapis.com/drive/v3";
        URI url = null;
        String res = "didnt work";
        String filesOnlyQuery = "not mimeType contains 'folder'";
        String uriString = urlBase;
        try {
            uriString = urlBase + "/files?fields="+URLEncoder.encode(FIELD_ITEMS,"UTF-8");
            uriString = uriString + "&q="+URLEncoder.encode(filesOnlyQuery,"UTF-8");;
        } catch (UnsupportedEncodingException ex) {
            LOG.error("Encoding problem "+ex.getMessage());
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

                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
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

            } catch (Exception iex) {

                res = "cannot do file list   problem " + iex.getMessage() + " " + iex.getClass().getName();
                LOG.error(res);
            }

        } else {
            LOG.error("url null");
        }

        model.addObject("result", res);
        model.addObject("files",fileItems);
        model.addObject("appTitle", "Google File API");
        model.setViewName("pages/fileList");
        return model;

    }

}
