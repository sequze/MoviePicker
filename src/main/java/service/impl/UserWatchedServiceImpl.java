package service.impl;

import dao.MovieDao;
import dao.UserWatchedDao;
import entity.Movie;
import service.UserWatchedService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserWatchedServiceImpl implements UserWatchedService {
    private final UserWatchedDao watchedDao;
    private final MovieDao movieDao;

    public UserWatchedServiceImpl(UserWatchedDao watchedDao, MovieDao movieDao) {
        this.watchedDao = watchedDao;
        this.movieDao = movieDao;
    }

    @Override
    public void markWatched(Long userId, Long movieId) {
        watchedDao.add(userId, movieId);
    }

    @Override
    public void unmarkWatched(Long userId, Long movieId) {
        watchedDao.remove(userId, movieId);
    }

    @Override
    public boolean isWatched(Long userId, Long movieId) {
        return watchedDao.exists(userId, movieId);
    }

    @Override
    public List<Long> getWatchedMovieIds(Long userId) {
        return watchedDao.findMovieIdsByUser(userId);
    }

    @Override
    public List<Movie> getWatchedMovies(Long userId) {
        List<Long> ids = watchedDao.findMovieIdsByUser(userId);
        List<Movie> res = new ArrayList<>();
        for (Long id : ids) {
            Optional<Movie> m = movieDao.findById(id);
            m.ifPresent(res::add);
        }
        return res;
    }
}
