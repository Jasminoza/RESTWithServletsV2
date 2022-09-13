package org.yolkin.service;

import org.yolkin.model.Event;
import org.yolkin.model.File;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.hibernate.HibernateEventRepositoryImpl;

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

    public Event create(Event event) {
        return eventRepository.create(event);
    }

    public Event getById(Long id) {
        return eventRepository.getById(id);
    }

    public Event update(Event event) {
        return eventRepository.update(event);
    }

    public void delete(Long id) {
        eventRepository.delete(id);
    }
}