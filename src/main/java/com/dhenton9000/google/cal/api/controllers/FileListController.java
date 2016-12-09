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
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
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

    private Client getJerseyClient() {
        Client client = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class)
                .build();
        return client;
    }

    @RequestMapping("/fileUpload")
    public ModelAndView fileUpLoad(ModelAndView model) {

        //http://stackoverflow.com/questions/21102071/resttemplate-upload-image-file
        String authToken = oAuth2RestTemplate.getAccessToken().getValue();
        ImageGenerator gen = new ImageGenerator();
        String res = "nothing happened";
        HttpStatus statusCode = HttpStatus.OK;
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

                byte[] byteData = gen.createImage("png", "Get another job!!!!!!");
                Client client = getJerseyClient();
                
                WebTarget target = client.target(uriString);
                target.register(new LoggingFilter( ));
                
                final MultiPart multiPartEntity = new MultiPart()
                        .bodyPart(new BodyPart( "{name: 'zzzfred.png'}", javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE)) 
                        .bodyPart(new BodyPart(byteData,javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM_TYPE) );
                      //  
                      LOG.info("hit 1");
                Response response = target.request()
                        .header("Authorization", "Bearer "+authToken)
                        .post(Entity.entity(multiPartEntity, multiPartEntity.getMediaType()));
                     LOG.info("hit 2");
                
                if (response.getStatus() != 200) {
                   LOG.error("ERROR response is "+response.getStatus()+" "+response.getEntity().getClass().getName());
                }
                else
                {
                    LOG.info("response is "+response.getStatus()+" "+response.getEntity().getClass().getName());
                }
            } catch (Exception iex) {

                res = "cannot do fileupload io problem " + iex.getMessage();
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
