package org.yolkin.model;

import java.util.List;

//@Entity
//@Table(name = "events")
public class Event {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column (name = "event_id")
    private Long eventId;

//    @OneToOne (cascade = CascadeType.ALL)
//    @JoinColumn (name = "file_id" , referencedColumnName = "id")
    private File file;

//    @ManyToOne
//    @JoinColumn (name = "user_id")
    private List<User> users;

    public Event() {
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long id) {
        this.eventId = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}