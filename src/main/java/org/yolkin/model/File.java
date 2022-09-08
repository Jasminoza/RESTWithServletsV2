package org.yolkin.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table (name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_uploading")
    private Date dateOfUploading;

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

    public Date getDateOfUploading() {
        return dateOfUploading;
    }

    public void setDateOfUploading(Date dateOfUploading) {
        this.dateOfUploading = dateOfUploading;
    }
}