/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dhenton
 */
@Controller
public class PowerPointController {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCalendarController.class);

    @RequestMapping(value = "/powerPointGraph", method = {RequestMethod.GET})
    public ModelAndView powerPointMainPage(ModelAndView model) {
        model.addObject("appTitle", "PowerPoint Demo");

        model.setViewName("pages/powerPointDemo/home");
        return model;

    }
}
