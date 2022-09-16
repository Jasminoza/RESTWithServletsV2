package org.yolkin.service;

import org.yolkin.model.Event;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.hibernate.HibernateEventRepositoryImpl;
import org.yolkin.util.ServiceHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;

public class EventService {

    private final EventRepository eventRepository;

    public EventService() {
        eventRepository = new HibernateEventRepositoryImpl();
    }

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAll() {
        return eventRepository.getAll();
    }

    public Event getById(String id, HttpServletRequest req, HttpServletResponse resp, String mappingUrl) throws IOException {
        ServiceHelper helper = new ServiceHelper(eventRepository, req, resp, mappingUrl);

        if (helper.evenServiceGetByIdRequestIsCorrect()) {
            return helper.getEventById(id);
        } else {
            return null;
        }
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp, String mappingUrl) throws IOException {

        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank()) {
            resp.sendError(SC_BAD_REQUEST, "Event id can't be null");
        } else {
            try {
                Long idFromRequest = Long.valueOf(id);
                Event event = eventRepository.getById(idFromRequest);

                if (event == null) {
                    resp.sendError(SC_NOT_FOUND, "There is no event with such id");
                } else {
                    eventRepository.delete(idFromRequest);
                    resp.setStatus(SC_NO_CONTENT);
                }
            } catch (NumberFormatException e) {
                resp.sendError(SC_BAD_REQUEST, "Incorrect event id");
            }
        }
    }
}