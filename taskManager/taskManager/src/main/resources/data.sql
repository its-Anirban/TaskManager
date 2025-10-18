CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  is_logged_in BOOLEAN DEFAULT FALSE
);

INSERT INTO users (username, password, is_logged_in) VALUES ('anirban', '1234', false);
INSERT INTO users (username, password, is_logged_in) VALUES ('aniruddha', '5678', false);
