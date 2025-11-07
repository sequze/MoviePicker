package service.impl;

import dao.GenreDao;
import entity.Genre;
import service.GenreService;

import java.util.List;
import java.util.Optional;

public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;

    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreDao.findAll();
    }

    @Override
    public Genre createGenre(String name) {
        Genre genre = new Genre(null, name);
        genreDao.save(genre);
        return genre;
    }

    @Override
    public void deleteGenre(Long genreId) {
        genreDao.deleteById(genreId);
    }

    @Override
    public void updateGenre(Long genreId, String newName) {
        genreDao.save(new Genre(genreId, newName));
    }
    @Override
    public Optional<Genre> getGenreById(Long genreId) {
        return genreDao.findById(genreId);
    }
}
