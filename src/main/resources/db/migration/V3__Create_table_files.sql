CREATE TABLE IF NOT EXISTS files (
      id                    INT AUTO_INCREMENT NOT NULL,
      name                  VARCHAR(255) NOT NULL,
      file_path             TEXT NOT NULL,
      date_of_uploading     DATETIME NOT NULL,
      PRIMARY KEY (id)
);