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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
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

    @RequestMapping("/fileList")
    public ModelAndView fileList(ModelAndView model) {

        String urlBase = "https://www.googleapis.com/drive/v3";
        URI url = null;
        String res = "didnt work";
        String uriString = urlBase + "/files";
        List<File> fileItems = new ArrayList<File>();

        try {
            url = new URI(uriString);
        } catch (URISyntaxException ex) {
            LOG.error("could not create uri " + uriString);
            res = "could not create uri " + uriString;
        }
        if (url != null) {
            
            try {

                //File ff = new com.google.api.services.drive.model.File();
                //  LOG.debug(input);
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                // HttpEntity<String> infoEntity = new HttpEntity<String>(input, headers);
               // LOG.info("a1");
                FileList fileList;
                

                ResponseEntity<String> responseOut = oAuth2RestTemplate.getForEntity(url, String.class);
               // LOG.info("a2");
                if (RestUtil.isError(responseOut.getStatusCode())) {

                    LOG.error("res is " + res);
                } else {
                    String dataIn = responseOut.getBody();
                    fileList = JSON_FACTORY.fromString(dataIn, FileList.class);

                    fileList.getFiles().forEach(f -> {
                        LOG.info(f.getName());

                    });
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
