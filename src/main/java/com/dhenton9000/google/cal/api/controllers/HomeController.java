/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.controllers;

import com.dhenton9000.google.cal.api.ClientResources;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.google.api.services.calendar.CalendarScopes;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @Autowired
    ClientResources clientResources;
    @Autowired
    OAuth2ClientContext oauth2ClientContext;
    
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/logout_done")
    public String logoutDone(Model model) {
        model.addAttribute("appTitle", "Logged Out");
        String t = CalendarScopes.CALENDAR;
        return "pages/logout_done";

    }

    @RequestMapping("/")
    public ModelAndView home(@RequestParam(value = "code", defaultValue = "not yet") String code, ModelAndView model) {
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
        String codeInfo = "not yet";
        if (code != null) {
            codeInfo = code.toString();
        }
        
       // LOG.debug(String.format ("resources %s context %s",clientResources, oauth2ClientContext));
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(clientResources.getClient(), oauth2ClientContext);
        // LOG.debug(oAuth2RestTemplate.toString());
        OAuth2AccessToken token = oAuth2RestTemplate.getAccessToken();
         LOG.debug(token.getTokenType()+" "+token.getValue());
        // UserInfoTokenServices tokenServices = new UserInfoTokenServices(clientResources.getResource().getUserInfoUri(),
        //         clientResources.getClient().getClientId());
        // tokenServices.setRestTemplate(oAuth2RestTemplate);
        String urlBase = "https://www.googleapis.com/calendar/v3";
        URI url = null;
        String res = "nothing";
        String uriString = urlBase + "/colors";
        try {
            url = new URI(uriString);
        } catch (URISyntaxException ex) {
             LOG.error("could not create uri "+uriString);
        }
        if (url != null)
           res = oAuth2RestTemplate.getForObject(url, String.class);
        else
            LOG.error("url null");
        
        LOG.info("xxxxxxx "+res);

        model.addObject("oauthInfo", info);
        model.addObject("loggedIn", loggedIn);
        model.addObject("appTitle", "Calendar API");
        model.addObject("codeInfo", codeInfo);
        model.setViewName("pages/home");
        return model;
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
