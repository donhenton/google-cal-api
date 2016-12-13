/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api;

import com.dhenton9000.google.rest.utils.RestUtil;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttachment;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author dhenton
 */
@Component
public class EventCreator {

    private static final Logger LOG = LoggerFactory.getLogger(EventCreator.class);
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    
    private final OAuth2RestTemplate oAuth2RestTemplate;
    
    public EventCreator(OAuth2RestTemplate t)
    {
        oAuth2RestTemplate = t;
    }

    private Event makeEvent(String dateString, 
            List<String> attendeeEmails,
            List<EventAttachment> attachments) {
        Event event = new Event()
                .setSummary("Report Waiting")
                .setLocation("Networked Insights")
                .setDescription("A report is waiting. Click on the source link above to access it.");
        event.setFactory(JSON_FACTORY);
        DateTime startDateTime = new DateTime(dateString + "T09:00:00-07:00");

        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);
        ArrayList<EventAttendee> eventAttendees = new ArrayList<EventAttendee>();
        if (attendeeEmails != null) {
            for (String email : attendeeEmails) {
                EventAttendee evAt = new EventAttendee();
                evAt.setEmail(email);
                eventAttendees.add(evAt);
            }
            if (eventAttendees.size() > 0) {
                event.setAttendees(eventAttendees);
            }
        }
        DateTime endDateTime = new DateTime(dateString + "T09:15:00-07:00");

        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);
         
       
        if (attachments != null)
        {
            event.setAttachments(attachments);
        }
        

        LOG.info("hit the prod");

        return event;

    }

    /**
     *
     * @param dateString in the form of MM/dd/yyyy
     * @param attendeeEmails emails of attendees send null if none
     * @param attachments the attachments send null if none
     * 
     * @return string representation of post output
     */
    public String createEvent(String dateString, 
            List<String> attendeeEmails,List<EventAttachment> attachments) throws IOException {

        SimpleDateFormat sdfInput = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdfInput.parse(dateString);
            dateString = sdfOutput.format(d);
            // LOG.debug("dateString " + dateString);
        } catch (ParseException ex) {
            // LOG.error("could not parse " + dateString);
        }

        String res = "nothing happened";
        String urlBase = "https://www.googleapis.com/calendar/v3";
        URI url = null;

        String uriString = urlBase + "/calendars/primary/events?";
        uriString = uriString + "supportsAttachments=true";
        uriString = uriString + "&sendNotifications=true";
        try {
            url = new URI(uriString);
        } catch (URISyntaxException ex) {
            LOG.error("could not create uri " + uriString);
            res = "could not create uri " + uriString;
        }
         
            Event evs = makeEvent(dateString, attendeeEmails,attachments);

            String input = evs.toPrettyString();
            //  LOG.debug(input);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> infoEntity = new HttpEntity<String>(input, headers);
            LOG.info("temp "+getoAuth2RestTemplate());
            ResponseEntity<String> responseOut
                    = getoAuth2RestTemplate().exchange(url,
                            HttpMethod.POST, infoEntity, String.class);
            res = responseOut.getBody();

            if (RestUtil.isError(responseOut.getStatusCode())) {

                LOG.error("res is " + res);
            }
 
        return res;
    }

    /**
     * @return the oAuth2RestTemplate
     */
    public OAuth2RestTemplate getoAuth2RestTemplate() {
        return oAuth2RestTemplate;
    }

}
