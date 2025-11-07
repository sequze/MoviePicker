INSERT INTO users (login, email, password_hash, role) VALUES
                                                          -- password 1234
                                                          ('admin', 'admin@example.com', 'x2/V1NgdefHKZQVxIZ3Hfw==:fbb7C2nPSTiIqQDNsOosHOoacF4VpnXpohqiJd3HhFw=', 'admin'),
                                                          ('user', 'user@example.com', 'x2/V1NgdefHKZQVxIZ3Hfw==:fbb7C2nPSTiIqQDNsOosHOoacF4VpnXpohqiJd3HhFw=', 'user');

INSERT INTO directors (name) VALUES
                                   ('Christopher Nolan'),
                                   ('Francis Ford Coppola'),
                                   ('Hayao Miyazaki');
INSERT INTO genres (name) VALUES
                                ('Sci-Fi'),
                                ('Crime'),
                                ('Animation');

INSERT INTO movies (name, description, director_id, genre_id, rating, poster_url, year) VALUES
                                                                                              ('Inception', 'A mind-bending thriller.', 1, 1, 8.8, 'https://image.tmdb.org/t/p/w500/inception.jpg', 2010),
                                                                                              ('The Godfather', 'Crime family saga.', 2, 2, 9.2, 'https://image.tmdb.org/t/p/w500/godfather.jpg', 2006),
                                                                                              ('Spirited Away', 'Animated fantasy adventure.', 3, 3, 8.6, 'https://image.tmdb.org/t/p/w500/spirited.jpg', 2005);

select movies.name, description, rating, poster_url, year, directors.name, genres.name from movies join directors on movies.director_id = directors.id join genres on movies.genre_id = genres.id where movies.id = 1;