package service;


import entity.Movie;
import java.util.List;

public interface UserWatchedService {
    void markWatched(Long userId, Long movieId);
    void unmarkWatched(Long userId, Long movieId);
    boolean isWatched(Long userId, Long movieId);
    List<Long> getWatchedMovieIds(Long userId);
    List<Movie> getWatchedMovies(Long userId);
}
