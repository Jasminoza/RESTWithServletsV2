CREATE TABLE IF NOT EXISTS users (
      id     INT AUTO_INCREMENT NOT NULL,
      name   VARCHAR(50) NOT NULL,
      PRIMARY KEY (id),
      UNIQUE (name)
);