/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.controllers;

import com.dhenton9000.google.rest.utils.RestUtil;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Event.Gadget;
import com.google.api.services.calendar.model.Event.Source;
import com.google.api.services.calendar.model.EventDateTime;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${spring.profiles.active}")
    private String activeEnv;

    @Value("${calendar.gadget.iconLink}")
    private String gadgetIconLink;

    @Value("${calendar.gadget.link}")
    private String gadgetLink;

    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final Logger LOG = LoggerFactory.getLogger(GoogleCalendarController.class);

    @RequestMapping(value = "/googleAction", method = {RequestMethod.POST})
    public ModelAndView googleAction(@RequestParam("dateString") String dateString, ModelAndView model) {

        //LOG.debug("dateString " + dateString);
        //11/15/2016
        SimpleDateFormat sdfInput = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdfInput.parse(dateString);
            dateString = sdfOutput.format(d);
            // LOG.debug("dateString " + dateString);
        } catch (ParseException ex) {
            // LOG.error("could not parse " + dateString);
        }

        String urlBase = "https://www.googleapis.com/calendar/v3";
        URI url = null;
        String res = "didnt work";
        String uriString = urlBase + "/calendars/primary/events";

        try {
            url = new URI(uriString);
        } catch (URISyntaxException ex) {
            LOG.error("could not create uri " + uriString);
            res = "could not create uri " + uriString;
        }
        if (url != null) {

            try {
                Event evs = makeEvent(dateString);

                String input = evs.toPrettyString();
                //  LOG.debug(input);

                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> infoEntity = new HttpEntity<String>(input, headers);
                ResponseEntity<String> responseOut
                        = oAuth2RestTemplate.exchange(url,
                                HttpMethod.POST, infoEntity, String.class);
                res = responseOut.getBody();

                if (RestUtil.isError(responseOut.getStatusCode())) {

                    LOG.error("res is " + res);
                }

            } catch (IOException iex) {

                res = "cannot do calendar list io problem " + iex.getMessage();
                LOG.error(res);
            }

        } else {
            LOG.error("url null");
        }

        model.addObject("appTitle", "Google Response");
        model.addObject("dateString", dateString);
        model.addObject("result", res);
        model.setViewName("pages/googleAction");

        return model;
    }

    private String computeGraphURL() {
        String portInfo = "";
        if (activeEnv.equals("dev")) {
            portInfo = ":" + serverPort;

        }
        return serverUrl + portInfo;
    }

    private Event makeEvent(String dateString) {
        Event event = new Event();
        // .setSummary("Report Waiting")
        //  .setLocation("Networked Insights")
        //  .setDescription("A report is waiting. Click on the source link above to access it.");
        event.setFactory(JSON_FACTORY);
        DateTime startDateTime = new DateTime(dateString + "T09:00:00-07:00");
        // DateTime startDateTime = new DateTime("2016-11-18T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);

        DateTime endDateTime = new DateTime(dateString + "T09:15:00-07:00");
        // DateTime endDateTime = new DateTime("2016-11-19T09:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        // The gadget will display a popup window with the icon as a link, but 
        // both the icon and the content need to be reached by https
        // if (activeEnv.equals("prod")) {
        Gadget g = new Gadget();
        //g.setHeight(150);
        // g.setWidth(300);
        g.setLink(gadgetLink);
        g.setIconLink(gadgetIconLink);
        g.setTitle("Test Link");
        g.setType("application/x-google-gadgets+xml");
        g.setDisplay("chip");
        g.setWidth(300);
        g.setHeight(136);

        Map<String, String> prefs = new HashMap<String, String>();
         prefs.put("Format", "0");
         prefs.put("Days", "1");
         g.setPreferences(prefs);
        event.setGadget(g);

        LOG.info("hit the prod");

        // }
//        String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
//        event.setRecurrence(Arrays.asList(recurrence));
/*
        EventAttendee[] attendees = new EventAttendee[]{
            new EventAttendee().setEmail("lpage@example.com"),
            new EventAttendee().setEmail("sbrin@example.com"),};
        event.setAttendees(Arrays.asList(attendees));
         */
        // Source source = new Source();
        // source.setTitle("Click on this link for the report");
        // source.setUrl(computeGraphURL() + "/graph");
        // event.setSource(source);

        /*
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
