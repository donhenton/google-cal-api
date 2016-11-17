/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.controllers;

import java.text.SimpleDateFormat;
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
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

     

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/logout_done")
    public String logoutDone(Model model) {
        model.addAttribute("appTitle", "Logged Out");
        return "pages/logout_done";

    }

    @RequestMapping("/")
    public ModelAndView home(ModelAndView model) {

        Date d = new Date();
        SimpleDateFormat sdfInput = new SimpleDateFormat("MM/dd/yyyy");
        String initialDate = sdfInput.format(d);
        model.addObject("appTitle", "Home Page");
        model.addObject("initialDate",initialDate);
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
