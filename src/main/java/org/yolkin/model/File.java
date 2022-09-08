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

    @OneToOne (mappedBy = "event")
    private Event event;
}