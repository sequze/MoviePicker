package service.impl;

import dao.GenreDao;
import entity.Genre;
import service.GenreService;

import java.util.List;

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
    public void createGenre(String name) {
        genreDao.save(new Genre(null, name));
    }

    @Override
    public void deleteGenre(Long genreId) {
        genreDao.deleteById(genreId);
    }

    @Override
    public void updateGenre(Long genreId, String newName) {
        genreDao.save(new Genre(genreId, newName));
    }
}
