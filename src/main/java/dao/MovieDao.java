package dao;

import entity.Movie;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MovieDao {
    List<Movie> findAll();
    Optional<Movie> findById(Long id);
    Movie save(Movie movie);
    boolean deleteById(Long id);
    List<Movie> findByFilters(Long genreId, Double minRating, Long directorId);
    Optional<Movie> findRandomByFilters(Long genreId, Double minRating, Long directorId);
    Map<String, Object> getFullMovieInformation(Long movieId);
}