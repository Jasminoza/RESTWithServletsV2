package org.yolkin.servlet;

import org.yolkin.model.Event;
import org.yolkin.model.File;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.hibernate.HibernateEventRepositoryImpl;
import org.yolkin.util.ServletHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<Event, List<File>> filesByEvents = new HashMap<>();
        for (Event event : events) {
            if (!filesByEvents.containsKey(event)) {
                filesByEvents.put(event, event.getFiles());
            } else {
                List<File> addedFiles = filesByEvents.get(event);
                for (File file : event.getFiles()) {
                    if (!addedFiles.contains(file)) {
                        addedFiles.add(file);
                    }
                }
                filesByEvents.put(event, addedFiles);
            }
        }

        filesByEvents.entrySet().forEach( entry ->
                {
                 helper.addToResponseBody(
                                 entry.getKey().toString() + entry.getValue().toString() + "\n\n");
                }
        );

        helper.sendResponse();
    }
}