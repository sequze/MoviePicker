package service.impl;

import dao.UserFavoriteDao;
import service.UserFavoriteService;

import java.util.List;

public class UserFavoriteServiceImpl implements UserFavoriteService {
    private final UserFavoriteDao userFavoriteDao;

    public UserFavoriteServiceImpl(UserFavoriteDao userFavoriteDao) {
        this.userFavoriteDao = userFavoriteDao;
    }

    @Override
    public void like(Long userId, Long movieId) {
        userFavoriteDao.addFavorite(userId, movieId);
    }

    @Override
    public void unlike(Long userId, Long movieId) {
        userFavoriteDao.removeFavorite(userId, movieId);
    }

    @Override
    public boolean isFavorite(Long userId, Long movieId) {
        return userFavoriteDao.exists(userId, movieId);
    }

    @Override
    public List<Long> getFavoriteMovieIds(Long userId) {
        return userFavoriteDao.findByUser(userId);
    }
}
