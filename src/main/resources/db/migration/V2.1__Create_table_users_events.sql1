CREATE TABLE IF NOT EXISTS users_events (
    user_id     INT NOT NULL,
    event_id     INT NOT NULL,
    UNIQUE (user_id, event_id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE ON UPDATE CASCADE
);