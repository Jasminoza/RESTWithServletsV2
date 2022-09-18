CREATE TABLE IF NOT EXISTS events (
      id            INT AUTO_INCREMENT NOT NULL,
      date          DATETIME NOT NULL,
      user_id       INT NOT NULL,
      event_type    INT NOT NULL,
      file_id       INT NOT NULL,
      PRIMARY KEY (id),
      CONSTRAINT fk0_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
      CONSTRAINT fk0_event_type FOREIGN KEY (event_type) REFERENCES events(id) ON UPDATE CASCADE ON DELETE CASCADE,
      CONSTRAINT fk0_file FOREIGN KEY (file_id) REFERENCES files(id) ON UPDATE CASCADE ON DELETE CASCADE
);