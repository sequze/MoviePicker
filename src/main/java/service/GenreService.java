package service;

import entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<Genre> getAllGenres();
    Genre createGenre(String name);
    void deleteGenre(Long genreId);
    void updateGenre(Long genreId, String newName);
    Optional<Genre> getGenreById(Long genreId);
}
