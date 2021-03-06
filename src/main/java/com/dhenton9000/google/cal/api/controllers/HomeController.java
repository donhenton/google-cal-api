/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.controllers;

import com.dhenton9000.google.cal.api.UserInfo;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.ConfigurableWebApplicationContext;

@Controller
public class HomeController {

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${spring.profiles.active}")
    private String activeEnv;

    @Autowired
    ConfigurableWebApplicationContext applicationContext;
    
     @Autowired
    OAuth2ClientContext oauth2ClientContext;

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/logout_done")
    public String logoutDone(Model model) {
        model.addAttribute("appTitle", "Logged Out");
        return "pages/logout_done";

    }

   

    /*
    
     public ModelAndView home(Principal principal , ModelAndView model) {

        Date d = new Date();
        String name = principal.getName();
        String className = principal.getClass().getName();
        LOG.info("XXXX "+className);
 
     */
    @RequestMapping("/")
    public ModelAndView home(Principal principal, ModelAndView model) {

        OAuth2RefreshToken refreshToken 
                = oauth2ClientContext.getAccessToken().getRefreshToken();
        String rToken = "refresh is null";
        if (refreshToken != null)
        {
            rToken = refreshToken.getValue();
            
        }
        
        
        
        Date d = new Date();
        
        if (applicationContext != null) {
            LOG.debug("start bean list");
            //applicationContext.getBeanDefinitionNames();; used to dump
            // bean names which can be used to lookup scope below

            BeanDefinition def = applicationContext
                    .getBeanFactory()
                    .getBeanDefinition("scopedTarget.oauth2ClientContext");
            
            LOG.debug("\"scopedTarget.oauth2ClientContext\" " + def.getScope());
        } else {
            LOG.debug("app context is null");
        }
        OAuth2Authentication auth = (OAuth2Authentication) principal;

        UserInfo userInfo = (UserInfo) auth.getUserAuthentication().getPrincipal();
        String totalUrl = serverUrl + ":" + serverPort + " " + activeEnv;
        SimpleDateFormat sdfInput = new SimpleDateFormat("MM/dd/yyyy");
        String initialDate = sdfInput.format(d);
        model.addObject("appTitle", "Home Page");
        model.addObject("initialDate", initialDate);
        model.addObject("totalUrl", totalUrl);
        model.addObject("userInfo", userInfo);
        model.addObject("refreshToken",rToken);
        model.setViewName("pages/home");
        return model;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/logout_done";
    }

}
