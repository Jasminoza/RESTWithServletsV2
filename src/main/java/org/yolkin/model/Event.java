package org.yolkin.model;

import jakarta.persistence.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users_files")
public class Event {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "file_id", referencedColumnName = "id")
//    @LazyCollection(LazyCollectionOption.FALSE)
//    private List<File> files;
//
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public List<File> getFiles() {
//        return files;
//    }
//
//    public void setFiles(List<File> files) {
//        this.files = files;
//    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Event event = (Event) o;
//        return Objects.equals(id, event.id) && Objects.equals(files, event.files) && Objects.equals(user, event.user);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, files, user);
//    }
}