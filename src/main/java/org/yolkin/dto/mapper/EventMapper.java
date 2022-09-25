package org.yolkin.dto.mapper;

import org.yolkin.dto.EventDTO;
import org.yolkin.model.EventEntity;

public class EventMapper {
    public static EventEntity toEvent(EventDTO eventDTO) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(eventDTO.getId());
        eventEntity.setDate(eventDTO.getDate());
        eventEntity.setUser(eventDTO.getUser());
        eventEntity.setEventType(eventDTO.getEventType());
        eventEntity.setFile(eventDTO.getFile());
        return eventEntity;
    }

    public static EventDTO toEventDTO(EventEntity eventEntity) {
        return new EventDTO(
                eventEntity.getId(),
                eventEntity.getDate(),
                eventEntity.getUser(),
                eventEntity.getEventType(),
                eventEntity.getFile()
        );
    }
}
