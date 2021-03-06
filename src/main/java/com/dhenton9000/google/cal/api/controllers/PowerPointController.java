package com.dhenton9000.google.cal.api.controllers;

import com.dhenton9000.docx4j.D3GraphBatikTransCoder;
import com.dhenton9000.docx4j.PowerPointGenerator;
import java.io.InputStream;
import java.util.HashMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dhenton
 */
@Controller
public class PowerPointController {

    private static final Logger LOG = LoggerFactory.getLogger(PowerPointController.class);

    @RequestMapping(value = "/powerPointGraph", method = {RequestMethod.GET})
    public ModelAndView powerPointMainPage(ModelAndView model, HttpServletRequest request) {
        model.addObject("appTitle", "PowerPoint Demo");
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
        model.addObject("basePath", basePath);
        model.setViewName("pages/powerPointDemo/home");
        return model;

    }

    @RequestMapping(value = "/pptDownload", method = {RequestMethod.POST})
    public void powerPointDownload(@RequestBody SVGForm svgForm, HttpServletResponse response) {

        response.setContentType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
        response.addHeader("content-disposition", "attachment; filename=presentation.pptx");

        LOG.debug(svgForm.toString());

        HashMap<String, String> mappings = new HashMap<String, String>();
       
        mappings.put("IMAGE_TITLE", svgForm.getImageTitle());
        mappings.put("IMAGE_TEXT", svgForm.getImageSubTitle());
        try {
            
            D3GraphBatikTransCoder tCoder = new D3GraphBatikTransCoder(svgForm.getSvgData());
            InputStream isImage = tCoder.getDocument();
            PowerPointGenerator gen = new PowerPointGenerator();
            ServletOutputStream outstream = response.getOutputStream();
            gen.generate(mappings, isImage, outstream,75.0f);
            
        } catch (Exception ex) {
            throw 
              new RuntimeException("error in download "+ex.getMessage()+"\n",ex);
        }

    }
    
    

}
