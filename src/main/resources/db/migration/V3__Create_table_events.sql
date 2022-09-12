CREATE TABLE IF NOT EXISTS events (
      id          INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
      event_id    INT NOT NULL,
      user_id     INT NOT NULL,
      file_id     INT NOT NULL,
      CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id),
      CONSTRAINT fk_file_id FOREIGN KEY (file_id) REFERENCES files(id)
);