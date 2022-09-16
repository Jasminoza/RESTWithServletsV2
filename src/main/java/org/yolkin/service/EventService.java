package org.yolkin.service;

import org.yolkin.model.Event;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.hibernate.HibernateEventRepositoryImpl;
import org.yolkin.util.ServiceHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

        if (helper.eventServiceGetByIdRequestIsCorrect()) {
            return helper.getEventById(id);
        } else {
            return null;
        }
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp, String mappingUrl) throws IOException {
        ServiceHelper helper = new ServiceHelper(eventRepository, req, resp, mappingUrl);

        if (helper.eventServiceDeleteRequestIsCorrect()) {
            helper.deleteEvent();
        }
    }
}