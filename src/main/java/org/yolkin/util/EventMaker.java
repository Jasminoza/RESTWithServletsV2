package org.yolkin.util;

import org.yolkin.model.EventEntity;
import org.yolkin.model.EventType;
import org.yolkin.model.FileEntity;
import org.yolkin.model.UserEntity;
import org.yolkin.repository.EventRepository;

import java.util.Date;

public class EventMaker {
    public static void makeCreateFileEvent(FileEntity fileEntity, UserEntity userEntity, EventRepository eventRepository) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setUser(userEntity);
        eventEntity.setDate(new Date());
        eventEntity.setEventType(EventType.CREATED);
        eventEntity.setFile(fileEntity);
        eventRepository.create(eventEntity);
    }

    public static void makeUpdateFileEvent(FileEntity fileEntity, UserEntity userEntity, EventRepository eventRepository) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setUser(userEntity);
        eventEntity.setDate(new Date());
        eventEntity.setEventType(EventType.UPDATED);
        eventEntity.setFile(fileEntity);
        eventRepository.create(eventEntity);
    }

    public static void makeDeleteFileEvent(FileEntity fileEntity, UserEntity userEntity, EventRepository eventRepository) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setUser(userEntity);
        eventEntity.setDate(new Date());
        eventEntity.setEventType(EventType.DELETED);
        eventEntity.setFile(fileEntity);
        eventRepository.create(eventEntity);
    }
}
