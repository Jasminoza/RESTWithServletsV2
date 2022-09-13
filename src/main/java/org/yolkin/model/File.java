package org.yolkin.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table (name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column
    private String filepath;

    @Column(name = "date_of_uploading")
    private Date dateOfUploading;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private List<Event> events;

    public File() {
    }

}