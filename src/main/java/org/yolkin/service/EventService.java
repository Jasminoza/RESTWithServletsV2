package org.yolkin.service;

import org.yolkin.dto.EventDTO;
import org.yolkin.model.EventEntity;
import org.yolkin.model.UserEntity;
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

    public List<EventEntity> getAll() {
        return eventRepository.getAll();
    }

    public EventDTO getById(Long id) {
        EventEntity event = eventRepository.getById(id);

        if (event == null) {
            return null;
        }
        //TODO::
        EventDTO eventDTO = new EventDTO(id);

        return eventDTO;
    }

    public void delete(Long id) {
        eventRepository.delete(id);
    }
}