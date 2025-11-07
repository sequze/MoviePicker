package service.impl;

import dao.MovieDao;
import dao.GenreDao;
import dao.DirectorDao;
import dto.MovieDTO;
import entity.Movie;
import entity.Genre;
import entity.Director;
import service.MovieService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MovieServiceImpl implements MovieService {
    private final MovieDao movieDao;
    private final GenreDao genreDao;
    private final DirectorDao directorDao;

    public MovieServiceImpl(MovieDao movieDao, GenreDao genreDao, DirectorDao directorDao) {
        this.movieDao = movieDao;
        this.genreDao = genreDao;
        this.directorDao = directorDao;
    }

    @Override
    public List<Movie> getAll() { return movieDao.findAll(); }

    @Override
    public Optional<MovieDTO> getById(Long id) {
        Map<String, Object> movie = movieDao.getFullMovieInformation(id);
        return movie != null ? Optional.of(new MovieDTO(movie)) : Optional.empty();
    }

    @Override
    public Optional<Movie> getEntityById(Long id) {
        return movieDao.findById(id);
    }

    @Override
    public Movie createOrUpdate(Movie movie) { return movieDao.save(movie); }

    @Override
    public boolean delete(Long id) { return movieDao.deleteById(id); }

    @Override
    public List<Movie> search(String genre, Double minRating, String director) {
        try {
            Long genreId = resolveGenreId(genre);
            Long directorId = resolveDirectorId(director);
            return movieDao.findByFilters(genreId, minRating, directorId);
        } catch (IllegalArgumentException e) {
            return Collections.emptyList(); // если был передан неизвестный жанр/режиссёр
        }
    }

    @Override
    public Optional<Movie> random(String genre, Double minRating, String director) {
        try {
            Long genreId = resolveGenreId(genre);
            Long directorId = resolveDirectorId(director);
            return movieDao.findRandomByFilters(genreId, minRating, directorId);
        } catch (IllegalArgumentException e) {
            return Optional.empty(); // если был передан неизвестный жанр/режиссёр
        }
    }
    private Long resolveGenreId(String genre) {
        if (genre == null || genre.trim().isEmpty()) return null;
        Optional<Genre> g = genreDao.findByName(genre);
        if (g.isPresent()) return g.get().getId();
        throw new IllegalArgumentException("Genre not found: " + genre);
    }
    private Long resolveDirectorId(String director) {
        if (director == null || director.trim().isEmpty()) return null;
        Optional<Director> d = directorDao.findByName(director);
        if (d.isPresent()) return d.get().getId();
        throw new IllegalArgumentException("Director not found: " + director);
    }

}