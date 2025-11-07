package dao;


import java.util.List;

public interface UserFavoriteDao {
    void addFavorite(Long userId, Long movieId);
    void removeFavorite(Long userId, Long movieId);
    boolean exists(Long userId, Long movieId);
    List<Long> findByUser(Long userId);
}
