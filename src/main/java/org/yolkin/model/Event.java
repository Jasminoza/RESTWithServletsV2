package org.yolkin.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;

    @Column(name = "event")
    private String event;

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}