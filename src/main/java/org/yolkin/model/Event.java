package org.yolkin.model;

import jakarta.persistence.*;

//@Entity
//@Table(name = "events")
public class Event {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column (name = "event_id")
    private Long id;

//    @OneToOne (cascade = CascadeType.ALL)
//    @JoinColumn (name = "file_id" , referencedColumnName = "id")
    private File file;

//    @ManyToOne
//    @JoinColumn (name = "user_id", nullable = false)
    private User user;

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}