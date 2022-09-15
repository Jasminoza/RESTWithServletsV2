package org.yolkin.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "file_path")
    private String filepath;

    @Column(name = "date_of_uploading")
    private Date dateOfUploading;

    @OneToOne(mappedBy = "file")
    private Event event;

    public File() {
    }

    public File(Long id, String name, String filepath, Date dateOfUploading) {
        this.id = id;
        this.name = name;
        this.filepath = filepath;
        this.dateOfUploading = dateOfUploading;
    }

    public File(String name, String filepath, Date dateOfUploading) {
        this.name = name;
        this.filepath = filepath;
        this.dateOfUploading = dateOfUploading;
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

    public Date getDateOfUploading() {
        return dateOfUploading;
    }

    public void setDateOfUploading(Date dateOfUploading) {
        this.dateOfUploading = dateOfUploading;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}