/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.controllers;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.DateTime;


import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

 */
/**
 *
 * @author dhenton
 */


@Controller
public class HomeController {

    @RequestMapping("/logout_done")
    public String logoutDone(Model model) {
        model.addAttribute("appTitle", "Logged Out");
        return "pages/logout_done";
    }

    @RequestMapping("/")
    public String home(Model model) {
        Object authObj = SecurityContextHolder.getContext().getAuthentication();
        boolean loggedIn = false;
        String info = "not found";
        if (authObj instanceof OAuth2Authentication) {
            OAuth2Authentication a = (OAuth2Authentication) authObj;
            
            if (a != null) {
                info = a.getName() + " xxxxx";
                loggedIn = true;
            }
        }

        model.addAttribute("oauthInfo", info);
        model.addAttribute("loggedIn",loggedIn);
        model.addAttribute("appTitle", "Calendar API");
        return "pages/home";
    }

    // Authentication a = SecurityContextHolder.getContext().getAuthentication();
//String clientId = ((OAuth2Authentication) a).getAuthorizationRequest().getClientId();
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/logout_done";
    }

}
