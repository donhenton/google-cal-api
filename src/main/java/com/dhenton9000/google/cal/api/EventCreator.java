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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

/**
 *
 * @author dhenton
 */

public class EventCreator {

    private static final Logger LOG = LoggerFactory.getLogger(EventCreator.class);
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    public static final SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    public static final SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private String timeZone = "America/Los_Angeles";
    private String timeZoneOffset = "-07:00";
    private String summary = "Summary Info";
    private String location = "Los Angeles";
    private String description = "Description";
    private String startTime = "09:15:00";
    private String endTime = "10:15:00";
    
    private OAuth2RestTemplate oAuth2RestTemplate;
    
    public EventCreator(OAuth2RestTemplate t)
    {
        oAuth2RestTemplate = t;
    }

    private Event makeEvent(String dateString, 
            List<String> attendeeEmails,
            List<EventAttachment> attachments) {
        Event event = new Event()
                .setSummary(getSummary())
                .setLocation(getLocation())
                .setDescription(getDescription());
        event.setFactory(JSON_FACTORY);
        DateTime startDateTime = new DateTime(dateString + "T"+getStartTime()+"-07:00");

        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(getTimeZone());
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
        DateTime endDateTime = new DateTime(dateString + "T"+getEndTime()+getTimeZoneOffset());

        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone(getTimeZone());
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

        
        try {
            Date d = INPUT_DATE_FORMAT.parse(dateString);
            dateString = OUTPUT_DATE_FORMAT.format(d);
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
    private OAuth2RestTemplate getoAuth2RestTemplate() {
        return oAuth2RestTemplate;
    }

    /**
     * @return the timeZone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * @param timeZone the timeZone to set
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * @return the timeZoneOffset
     */
    public String getTimeZoneOffset() {
        return timeZoneOffset;
    }

    /**
     * @param timeZoneOffset the timeZoneOffset to set
     */
    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

   

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
