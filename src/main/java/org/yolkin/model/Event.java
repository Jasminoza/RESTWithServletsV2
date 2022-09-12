package org.yolkin.model;

import jakarta.persistence.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;

    @Column(name = "event_id")
    private Long eventId;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "events",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> users;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "events",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "file_id")}
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<File> files;

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}