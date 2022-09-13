CREATE TABLE IF NOT EXISTS events (
    id          INT AUTO_INCREMENT NOT NULL,
    user_id     INT NOT NULL,
    file_id     INT NOT NULL,
    PRIMARY KEY (id)
);