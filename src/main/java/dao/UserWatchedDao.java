package dao;



import java.util.List;

public interface UserWatchedDao {
    void add(Long userId, Long movieId);
    void remove(Long userId, Long movieId);
    boolean exists(Long userId, Long movieId);
    List<Long> findMovieIdsByUser(Long userId);
}