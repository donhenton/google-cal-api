/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.controllers;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Event.Source;
import com.google.api.services.calendar.model.EventDateTime;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dhenton
 */
@Controller
public class GoogleCalendarController {

    @Autowired
    OAuth2RestTemplate oAuth2RestTemplate;

    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCalendarController.class);

    @RequestMapping(value = "/googleAction", method = {RequestMethod.POST})
    public ModelAndView googleAction(@RequestParam("information") String information, ModelAndView model) {

        String urlBase = "https://www.googleapis.com/calendar/v3";
        URI url = null;
        String res = "didnt work";
        //id = expcalendar1000@gmail.com;
        // String uriString = urlBase + "/users/me/calendarList/primary";
        String uriString = urlBase + "/calendars/primary/events";
        try {
            url = new URI(uriString);
        } catch (URISyntaxException ex) {
            LOG.error("could not create uri " + uriString);
            res = "could not create uri " + uriString;
        }
        if (url != null) {

            // res = oAuth2RestTemplate.getForObject(url, String.class);
            try {
                Event evs = makeEvent();

                evs.setDescription("Test request");
                Source source = new Source();
                source.setTitle("The source");
                source.setUrl("http://donhenton.com");
                // evs.setSource(source);
                String input = evs.toPrettyString();
                //  LOG.debug(input);

                // String input = "";
                // URI uu = oAuth2RestTemplate.postForLocation(url, input);
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> infoEntity = new HttpEntity<String>(input, headers);
                ResponseEntity<String> responseOut = 
                        oAuth2RestTemplate.exchange(url, 
                                HttpMethod.POST, infoEntity, String.class);
                res =  responseOut.getBody();
                // res = oAuth2RestTemplate.postForObject(url, input, String.class);
                //  res = oAuth2RestTemplate.getForObject(url, String.class);
            } catch (Exception ex) {
                res = "cannot do calendar list " + ex.getMessage() + " " + ex.getClass().getName();
                LOG.error(res);
            }

        } else {
            LOG.error("url null");
        }

        model.addObject("appTitle", "Google Response");
        model.addObject("information", information);
        model.addObject("result", res);
        model.setViewName("pages/googleAction");

        return model;
    }

    private Event makeEvent() {
        Event event = new Event()
                .setSummary("Google I/O 2016")
                .setLocation("800 Howard St., San Francisco, CA 94103")
                .setDescription("A chance to hear more about Google's developer products.");
        event.setFactory(JSON_FACTORY);
        DateTime startDateTime = new DateTime("2016-11-17T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);

        DateTime endDateTime = new DateTime("2016-11-17T17:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);
        /*
        String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));

        EventAttendee[] attendees = new EventAttendee[]{
            new EventAttendee().setEmail("lpage@example.com"),
            new EventAttendee().setEmail("sbrin@example.com"),};
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[]{
            new EventReminder().setMethod("email").setMinutes(24 * 60),
            new EventReminder().setMethod("popup").setMinutes(10),};
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);
         */
        return event;

    }
}
