package org.yolkin.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "file_path")
    private String filepath;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY)
    private List<Event> events;

    public File() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return Objects.equals(id, file.id) && Objects.equals(name, file.name) && Objects.equals(filepath, file.filepath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, filepath);
    }
}