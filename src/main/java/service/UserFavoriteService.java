package service;

import java.util.List;

public interface UserFavoriteService {
    void like(Long userId, Long movieId);
    void unlike(Long userId, Long movieId);
    boolean isFavorite(Long userId, Long movieId);
    List<Long> getFavoriteMovieIds(Long userId);

}
