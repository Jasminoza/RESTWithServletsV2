package org.yolkin.dto;

import org.yolkin.model.EventEntity;

import java.util.List;

public class FileDTO {
    private Long id;
    private String name;
    private String filePath;
    private List<EventEntity> events;

    public FileDTO(Long id, String name, String filePath, List<EventEntity> events) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<EventEntity> getEvents() {
        return events;
    }

    public void setEvents(List<EventEntity> events) {
        this.events = events;
    }
}
