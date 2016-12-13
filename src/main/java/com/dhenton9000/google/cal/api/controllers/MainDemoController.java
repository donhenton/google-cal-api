/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.controllers;

import com.dhenton9000.google.cal.api.ApptModel;
import com.dhenton9000.google.cal.api.EventCreator;
import com.dhenton9000.google.cal.api.MainModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainDemoController {

    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    private static final Logger LOG = LoggerFactory.getLogger(MainDemoController.class);

    @RequestMapping("/mainDemo")
    public ModelAndView mainDemoHome(ModelAndView model) {

        Date d = new Date();
        MainModel mModel = new MainModel();
        String initialDate = EventCreator.INPUT_DATE_FORMAT.format(d);
        mModel.setDateField(initialDate);
        model.addObject("appTitle", "Main Demonstration");
        model.addObject("mainModel",mModel);
        model.setViewName("pages/mainDemo/mainDemoHome");
        return model;

    }

    @RequestMapping(path = "/mainDemoPost", method = RequestMethod.POST)
    public ModelAndView mainDemoPost(@ModelAttribute("mainModel") MainModel mainModel, BindingResult result, ModelAndView model) {
        model.addObject("appTitle", "Main Demonstration");
        model.setViewName("pages/mainDemo/mainResult");
        return model;

    }

}
