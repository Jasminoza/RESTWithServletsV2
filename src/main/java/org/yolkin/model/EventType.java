package org.yolkin.model;

public enum EventType {
    CREATED(1L),
    UPDATED(2L),
    DELETED(3L);

    private final Long id;

    EventType(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}


