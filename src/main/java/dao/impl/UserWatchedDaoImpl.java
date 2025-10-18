package dao.impl;

import dao.UserWatchedDao;
import entity.UserWatched;
import util.DataSourceManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserWatchedDaoImpl implements UserWatchedDao {
    @Override
    public void add(Long userId, Long movieId) {
        String sql = "INSERT INTO user_watched(user_id, movie_id) VALUES (?, ?) ON CONFLICT (user_id, movie_id) DO NOTHING";
        try (Connection c = DataSourceManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, movieId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Long userId, Long movieId) {
        String sql = "DELETE FROM user_watched WHERE user_id = ? AND movie_id = ?";
        try (Connection c = DataSourceManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, movieId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists(Long userId, Long movieId) {
        String sql = "SELECT 1 FROM user_watched WHERE user_id = ? AND movie_id = ?";
        try (Connection c = DataSourceManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Long> findMovieIdsByUser(Long userId) {
        String sql = "SELECT movie_id FROM user_watched WHERE user_id = ? ORDER BY movie_id";
        try (Connection c = DataSourceManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Long> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(rs.getLong("movie_id"));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}