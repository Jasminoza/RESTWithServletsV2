package org.yolkin.servlet;

import org.yolkin.model.Event;
import org.yolkin.model.File;
import org.yolkin.model.User;
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
            helper.addH2ToResponseBody("Event ID: " + event.getEventId());

            for (User user : event.getUsers()) {
                helper.addH3ToResponseBody("User ID: " + user.getId());
                helper.addH3ToResponseBody("User name: " + user.getName());

                for (File file : event.getFiles()) {
                    helper.addToResponseBody("File ID: " + file.getId());
                    helper.addToResponseBody("File name: " + file.getName());
                    helper.addToResponseBody("File date of uploading: " + file.getDateOfUploading());
                    helper.addToResponseBody("<br/>");
                }
            }
        }

        helper.sendResponse();
    }
}