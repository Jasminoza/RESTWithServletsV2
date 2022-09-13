package org.yolkin.servlet;

import org.yolkin.model.Event;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.hibernate.HibernateEventRepositoryImpl;
import org.yolkin.util.ServletHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EventServlet extends HttpServlet {

    private static EventRepository eventRepository;

    public void init() {
        eventRepository = new HibernateEventRepositoryImpl();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletHelper helper = new ServletHelper(response);

        Long idFromRequest;

        try {
            idFromRequest = Long.valueOf(request.getHeader("event_id"));
        } catch (Exception e) {
            helper.sendBadRequestStatus("Incorrect event id.");
            return;
        }

        Event event = eventRepository.getById(idFromRequest);

        if (event == null) {
            helper.sendBadRequestStatus("There is no event with such id.");
            return;
        }

        helper.addH1ToResponseBody("Event details");
        helper.addToResponseBody("Event ID: " + event.getId());
        helper.addToResponseBody("Event files: " + event.getFiles());

        helper.sendResponse();
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletHelper helper = new ServletHelper(response);

        Long idFromRequest;

        try {
            idFromRequest = Long.valueOf(request.getHeader("event_id"));
        } catch (Exception e) {
            helper.sendBadRequestStatus("Incorrect event id.");
            return;
        }

        Event event = eventRepository.getById(idFromRequest);

        if (event == null) {
            helper.sendBadRequestStatus("There is no event with such id.");
            return;
        }

        eventRepository.delete(idFromRequest);

        helper.setResponseHead("Event details");
        helper.addH1ToResponseBody("Event with id " + event.getId() + " was deleted successfully.");
        helper.sendResponse();
    }
}