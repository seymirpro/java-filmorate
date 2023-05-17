CREATE TABLE IF NOT EXISTS "user" (
    id INTEGER  AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR,
    login VARCHAR,
    name VARCHAR,
    birthday date
);

CREATE TABLE IF NOT EXISTS status (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS user_friends (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id INTEGER,
    friend_id INTEGER,
    status_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES "user"(id),
    FOREIGN KEY (friend_id) REFERENCES "user"(id),
    FOREIGN KEY (status_id) REFERENCES status(id)
);

CREATE TABLE IF NOT EXISTS rating (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS films (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR,
    description VARCHAR(200),
    rating_id INTEGER,
    release_date DATE,
    duration INTEGER,
    created_at TIMESTAMP,
    FOREIGN KEY (rating_id) REFERENCES rating(id)
);

CREATE TABLE IF NOT EXISTS genre (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS film_genre (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    film_id INTEGER,
    genre_id INTEGER,
    FOREIGN KEY(film_id) REFERENCES films(id),
    FOREIGN KEY(genre_id) REFERENCES genre(id)
);

CREATE TABLE IF NOT EXISTS film_likes (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    film_id INTEGER,
    user_id INTEGER,
    FOREIGN KEY(film_id) REFERENCES films(id),
    FOREIGN KEY(user_id) REFERENCES "user"(id)
)



