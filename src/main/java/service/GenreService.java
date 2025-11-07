package service;

import entity.Genre;

import java.util.List;

public interface GenreService {
    public List<Genre> getAllGenres();
    public void createGenre(String name);
    public void deleteGenre(Long genreId);
    public void updateGenre(Long genreId, String newName);
}
