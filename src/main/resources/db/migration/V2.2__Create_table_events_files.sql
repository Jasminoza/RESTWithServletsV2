CREATE TABLE IF NOT EXISTS events_files (
    event_id     INT NOT NULL,
    file_id     INT NOT NULL,
    UNIQUE (event_id, file_id),
    CONSTRAINT fk1_event_id FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk1_file_id FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE CASCADE ON UPDATE CASCADE
);