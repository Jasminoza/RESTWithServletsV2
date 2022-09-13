package org.yolkin.servlet;

import org.yolkin.model.Event;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.hibernate.HibernateEventRepositoryImpl;
import org.yolkin.util.ServletHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class EventsServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletHelper helper = new ServletHelper(response);

        EventRepository eventRepository = new HibernateEventRepositoryImpl();
        List<Event> events = eventRepository.getAll();
        helper.setResponseHead("Events list");

        if (events.size() == 0) {
            helper.addToResponseBody("There is no events in database.");
            helper.sendResponse();
            return;
        }

        helper.addH1ToResponseBody("EVENTS DATA");

        for (Event event : events) {
            helper.addH3ToResponseBody("Event ID: " + event.getId());
            helper.addToResponseBody("Event files: " + event.getFiles());
            helper.addToResponseBody("<br/>");
        }

        helper.sendResponse();
    }
}