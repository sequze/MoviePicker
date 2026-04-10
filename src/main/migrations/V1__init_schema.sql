-- Schema for MoviePicker
CREATE TYPE user_role AS ENUM ('user', 'admin');
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       login VARCHAR(64) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role user_role NOT NULL DEFAULT 'user',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE genres (
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE directors (
                           id SERIAL PRIMARY KEY,
                           name varchar(100)
);

CREATE TABLE movies (
                        id SERIAL PRIMARY KEY,
                        name varchar(255) NOT NULL,
                        description TEXT,
                        director_id INTEGER,
                        genre_id INTEGER,
                        rating DECIMAL(3,1), -- 0.0..10.0
                        poster_url VARCHAR(500),
                        year INTEGER,
                        CONSTRAINT fk_movie_genre FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE SET NULL,
                        CONSTRAINT fk_movie_director FOREIGN KEY (director_id) REFERENCES directors(id) ON DELETE SET NULL
);



CREATE TABLE user_favorites (
                                user_id INTEGER NOT NULL,
                                movie_id INTEGER NOT NULL,
                                PRIMARY KEY (user_id, movie_id),
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_fav_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                CONSTRAINT fk_fav_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE user_watched (
    user_id INTEGER NOT NULL,
    movie_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, movie_id),
    CONSTRAINT fk_fav_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_fav_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
)