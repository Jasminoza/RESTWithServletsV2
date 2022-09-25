package org.yolkin.dto;

import org.yolkin.model.EventEntity;

import java.util.List;

public class UserDTO {
    Long id;
    String name;
    List<EventEntity> events;

    public UserDTO(Long id, String name, List<EventEntity> events) {
        this.id = id;
        this.name = name;
        this.events = events;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EventEntity> getEvents() {
        return events;
    }

    public void setEvents(List<EventEntity> events) {
        this.events = events;
    }
}
