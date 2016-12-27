/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.controllers;

import com.dhenton9000.google.cal.api.EventCreator;
import com.dhenton9000.google.cal.api.CalendarModel;
import com.dhenton9000.google.drive.FileReturnInfo;
import com.dhenton9000.google.drive.GoogleDriveWriter;
import com.dhenton9000.google.drive.TemplateGenerator;
import com.dhenton9000.google.rest.utils.RestUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.model.EventAttachment;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CalendarDemoController {

    @Autowired
    OAuth2RestTemplate oAuth2RestTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(CalendarDemoController.class);
    private static final String TEMPLATE_PATH = "/docx_templates/main_template.docx";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @RequestMapping("/calendarDemo")
    public ModelAndView calendarDemoHome(ModelAndView model) {

        Date d = new Date();
        CalendarModel mModel = new CalendarModel();
        String initialDate = EventCreator.INPUT_DATE_FORMAT.format(d);
        mModel.setDateField(initialDate);
        model.addObject("appTitle", "Calendar Invitation Demo");
        model.addObject("mainModel", mModel);
        model.setViewName("pages/calendarDemo/calendarDemoHome");
        return model;

    }

    @RequestMapping(path = "/calendarDemoPost", method = RequestMethod.POST)
    public ModelAndView mainDemoPost(@ModelAttribute("calenderModel") CalendarModel apptModel, 
            BindingResult result, ModelAndView model) {

        //http://stackoverflow.com/questions/21102071/resttemplate-upload-image-file
        String res = "nothing happened";

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
                GoogleDriveWriter writer = new GoogleDriveWriter();
                TemplateGenerator gen = new TemplateGenerator();
                Properties replacementProps = apptModel.getProperties();

                String fileName = apptModel.getFileName();

                byte[] byteData
                        = gen.replaceToByteArray(replacementProps, TEMPLATE_PATH);
                String jsonMetaData = "{name: \"" + fileName + ".docx\"}";

                HttpEntity<MultiValueMap<String, Object>> infoEntity
                        = writer.produceEntity(byteData, jsonMetaData, GoogleDriveWriter.DOCX_MEDIA_TYPE);

                ResponseEntity<String> responseOut
                        = oAuth2RestTemplate.exchange(url,
                                HttpMethod.POST, infoEntity, String.class);
                res = responseOut.getBody();
                if (RestUtil.isError(responseOut.getStatusCode())) {
                    LOG.error("res is " + res);
                } else {

                    //get the link for the attachment
                    JsonNode bodyObj = getFileInfo(res);
                    String viewLink = bodyObj.get("webViewLink").asText(null);
                    String fileId =   bodyObj.get("id").asText(null);
                    
                    EventCreator evCreator = new EventCreator(oAuth2RestTemplate);
                    
                    List<String> attendeeEmails = new ArrayList<String>();
                    attendeeEmails.add(apptModel.getFirstAttendee());
                    attendeeEmails.add(apptModel.getSecondAttendee());
                    List<EventAttachment> attachments = new ArrayList<EventAttachment>();

                    EventAttachment eA = new EventAttachment();
                    eA.setTitle("Meeting Agenda");
                    eA.setFileUrl(viewLink);
                    eA.setMimeType("application/vnd.google-apps.document");
                    eA.setFileId(fileId);
                    attachments.add(eA);
                    res = evCreator.createEvent(apptModel.getDateField(), attendeeEmails, attachments);

                }

            } catch (Exception iex) {

                res = "cannot do file upload  problem " + iex.getMessage();
                LOG.error(res);
            }

        } else {
            res = "Url could not be parsed";
        }

        LOG.debug("res\n"+res);
        model.addObject("appTitle", "Main Demonstration");
        model.addObject("result",res);
        model.setViewName("pages/mainDemo/mainResult");
        return model;

    }

    /**
     * get the link from the result of the file write.
     *
     * @param res
     * @return the webviewLink or null
     * @throws runtime on problems
     */
    private JsonNode getFileInfo(String res) {

        FileReturnInfo fileResult;
        try {
            fileResult = JSON_FACTORY.fromString(res, FileReturnInfo.class);
            String urlForFile = "https://www.googleapis.com/drive/v3/files/" + fileResult.getId() + "?fields=id,createdTime,webViewLink";
            ResponseEntity<JsonNode> responseFile = oAuth2RestTemplate.getForEntity(urlForFile, JsonNode.class);
            JsonNode bodyObj = responseFile.getBody();
            return bodyObj;
        } catch (Exception ex) {
            throw new RuntimeException("problem with getWebViewLink " + ex.getMessage());
        }

    }

}
