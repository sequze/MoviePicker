package dao.impl;

import dao.UserFavoriteDao;
import entity.UserFavorite;
import util.DataSourceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserFavoriteDaoImpl implements UserFavoriteDao {
    @Override
    public void addFavorite(Long userId, Long movieId) {
        String sql = "INSERT INTO user_favorites(user_id, movie_id) VALUES(?,?) ON CONFLICT DO NOTHING";
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
    public void removeFavorite(Long userId, Long movieId) {
        String sql = "DELETE FROM user_favorites WHERE user_id=? AND movie_id=?";
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
    public List<Long> findByUser(Long userId) {
        String sql = "SELECT movie_id FROM user_favorites WHERE user_id=? ORDER BY created_at DESC";
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