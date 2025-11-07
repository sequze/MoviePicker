package service;

import dto.MovieDTO;
import entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> getAll();
    Movie createOrUpdate(Movie movie);
    boolean delete(Long id);
    Optional<MovieDTO> getById(Long id);
    Optional<Movie> getEntityById(Long id);

    List<Movie> search(String genre, Double minRating, String director);
    Optional<Movie> random(String genre, Double minRating, String director);
}