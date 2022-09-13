CREATE TABLE IF NOT EXISTS files (
      id                    INT AUTO_INCREMENT NOT NULL,
      name                  VARCHAR(255) NOT NULL,
      date_of_uploading     DATETIME NOT NULL,
      user_id               INT NOT NULL,
      PRIMARY KEY (id),
      CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);