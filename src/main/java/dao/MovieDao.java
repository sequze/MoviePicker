package dao;

import entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieDao {
    List<Movie> findAll();
    Optional<Movie> findById(Long id);
    Movie save(Movie movie); // create or update based on id
    boolean deleteById(Long id);
    List<Movie> findByFilters(String genre, Double minRating, String director);
    Optional<Movie> findRandomByFilters(String genre, Double minRating, String director);
}