package org.yolkin.model;

import jakarta.persistence.*;

@Entity
@Embeddable
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}