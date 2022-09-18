package org.yolkin.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;

    @Column(name = "date")
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "event_type")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

    public Event() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
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