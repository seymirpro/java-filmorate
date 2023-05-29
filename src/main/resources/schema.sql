DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR,
    login VARCHAR,
    name VARCHAR,
    birthday DATE
);

DROP TABLE IF EXISTS status CASCADE;
CREATE TABLE  status (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR
);

DROP TABLE IF EXISTS user_friends CASCADE;
CREATE TABLE  user_friends (
    user_id INTEGER,
    friend_id INTEGER,
    status_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (friend_id) REFERENCES users(id),
    FOREIGN KEY (status_id) REFERENCES status(id),
    PRIMARY KEY (user_id, friend_id)
);

DROP TABLE IF EXISTS rating CASCADE;
CREATE TABLE  rating (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR
);

INSERT INTO rating (name) VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');

DROP TABLE IF EXISTS  films CASCADE;
CREATE TABLE  films (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR,
    description VARCHAR(200),
    rating_id INTEGER,
    release_date DATE,
    duration INTEGER,
    created_at TIMESTAMP,
    FOREIGN KEY (rating_id) REFERENCES rating(id)
);

DROP TABLE IF EXISTS genre CASCADE;
CREATE TABLE  genre (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR
);

DROP TABLE IF EXISTS film_genre CASCADE;
CREATE TABLE  film_genre (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id INTEGER,
    genre_id INTEGER,
    FOREIGN KEY(film_id) REFERENCES films(id),
    FOREIGN KEY(genre_id) REFERENCES genre(id)
);

DROP TABLE IF EXISTS film_likes CASCADE;
CREATE TABLE  film_likes (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id INTEGER,
    user_id INTEGER,
    FOREIGN KEY(film_id) REFERENCES films(id),
    FOREIGN KEY(user_id) REFERENCES users(id)
)



