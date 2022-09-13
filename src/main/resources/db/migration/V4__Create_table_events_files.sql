CREATE TABLE events_files (
      event_id    INT NOT NULL,
      file_id     INT NOT NULL,
      PRIMARY KEY (event_id, file_id),
      CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE ON UPDATE CASCADE,
      CONSTRAINT fk_file_id FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE CASCADE ON UPDATE CASCADE
);