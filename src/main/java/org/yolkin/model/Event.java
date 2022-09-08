package org.yolkin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "event_id")
    private Long id;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "file_id" , referencedColumnName = "id")
    private File file;

    @ManyToOne
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

}