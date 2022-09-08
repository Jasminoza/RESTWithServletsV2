CREATE TABLE IF NOT EXISTS events (
      event_id    INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
      user_id     INT NOT NULL,
      file_id     INT NOT NULL,
      CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id),
      CONSTRAINT fk_file_id FOREIGN KEY (file_id) REFERENCES files(id)
);