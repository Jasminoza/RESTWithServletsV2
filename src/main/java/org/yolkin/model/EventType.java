package org.yolkin.model;

public enum EventType {
    UNUSED  (0),
    CREATED (1),
    UPDATED (2),
    DELETED (3);

    private final int id;

    EventType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}


