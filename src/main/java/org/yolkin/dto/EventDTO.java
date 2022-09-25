package org.yolkin.dto;

import org.yolkin.model.EventType;
import org.yolkin.model.FileEntity;
import org.yolkin.model.UserEntity;

import java.util.Date;

public class EventDTO {
    private Long id;
    private Date date;
    private UserEntity user;
    private EventType eventType;
    private FileEntity file;

    public EventDTO(Long id, Date date, UserEntity user, EventType eventType, FileEntity file) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.eventType = eventType;
        this.file = file;
    }

    public EventDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public FileEntity getFile() {
        return file;
    }

    public void setFile(FileEntity file) {
        this.file = file;
    }
}