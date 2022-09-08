CREATE TABLE IF NOT EXISTS files (
      id                    INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
      name                  VARCHAR(255) NOT NULL,
      date_of_uploading     DATETIME NOT NULL
);