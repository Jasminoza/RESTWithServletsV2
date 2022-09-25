package org.yolkin.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "file_path")
    private String filepath;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY)
    private List<EventEntity> events;

    public FileEntity() {
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

    public List<EventEntity> getEvents() {
        return events;
    }

    public void setEvents(List<EventEntity> eventEntities) {
        this.events = eventEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEntity fileEntity = (FileEntity) o;
        return Objects.equals(id, fileEntity.id) && Objects.equals(name, fileEntity.name) && Objects.equals(filepath, fileEntity.filepath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, filepath);
    }
}