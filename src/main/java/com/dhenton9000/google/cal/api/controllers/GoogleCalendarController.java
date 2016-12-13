/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.controllers;

import com.dhenton9000.google.cal.api.EventCreator;
import com.dhenton9000.google.cal.api.UserInfo;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.model.EventAttachment;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dhenton
 */
@Controller
public class GoogleCalendarController {

    @Autowired
    OAuth2RestTemplate oAuth2RestTemplate;

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${spring.profiles.active}")
    private String activeEnv;


    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final Logger LOG = LoggerFactory.getLogger(GoogleCalendarController.class);

    @RequestMapping(value = "/googleAction", method = {RequestMethod.POST})
    public ModelAndView googleAction(Principal principal, @RequestParam("dateString") String dateString, ModelAndView model) {

        //LOG.debug("dateString " + dateString);
        //11/15/2016
         OAuth2Authentication auth = (OAuth2Authentication) principal;

        UserInfo userInfo = (UserInfo) auth.getUserAuthentication().getPrincipal();
            String res = "started";
            try 
            {
                EventCreator evCreator = new EventCreator(oAuth2RestTemplate);
                List<String> attendeeEmails = new ArrayList<String>();
                attendeeEmails.add("donhenton@gmail.com");
                attendeeEmails.add("donaby.henton@networkedinsights.com");
                List<EventAttachment> attachments = new ArrayList<EventAttachment>();
                
                EventAttachment eA = new EventAttachment();
                eA.setTitle("stuff to click on");
                eA.setFileUrl("https://docs.google.com/document/d/1fNUs_GR4sh7EoKWNKewG4LxOBXafM2FmE10NCM7GjBs/edit?usp=drivesdk");
                eA.setMimeType("application/vnd.google-apps.document");
                attachments.add(eA);
                eA.setFileId("1fNUs_GR4sh7EoKWNKewG4LxOBXafM2FmE10NCM7GjBs");
                
                res = evCreator.createEvent(dateString, attendeeEmails, attachments);

            } catch (IOException iex) {

                res = "cannot do calendar list io problem " + iex.getMessage();
                LOG.error(res);
            }

         

        model.addObject("appTitle", "Google Response");
        model.addObject("dateString", dateString);
        model.addObject("userName",userInfo.getName());
        
        model.addObject("result", res);
        model.setViewName("pages/googleAction");

        return model;
    }

    private String computeGraphURL() {
        String portInfo = "";
        if (activeEnv.equals("dev")) {
            portInfo = ":" + serverPort;

        }
        return serverUrl + portInfo;
    }

    
}
