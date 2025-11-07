package service;

import entity.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getAllGenres();
    void createGenre(String name);
    void deleteGenre(Long genreId);
    void updateGenre(Long genreId, String newName);
}
