CREATE TABLE IF NOT EXISTS events (
    id          INT AUTO_INCREMENT NOT NULL,
    user_id     INT NOT NULL,
    file_id     INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);