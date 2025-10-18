package dao.impl;

import dao.UserDao;

import java.sql.*;
import java.util.Optional;

import entity.Role;
import entity.User;
import util.DataSourceManager;

public class UserDaoImpl implements UserDao {

    @Override
    public Optional<User> findByLogin(String login) {
        String sql = "SELECT id, login, email, password_hash, role, created_at FROM users WHERE login = ?";
        try (Connection c = DataSourceManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = map(rs);
                    return Optional.of(u);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByLogin(String login) {
        String sql = "SELECT 1 FROM users WHERE login = ?";
        try (Connection c = DataSourceManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            String sql = "INSERT INTO users(login, email, password_hash, role) VALUES(?,?,?,?)";
            try (Connection c = DataSourceManager.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getLogin());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPasswordHash());
                ps.setString(4, user.getRole().name());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) user.setId(keys.getLong(1));
                }
                return user;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String sql = "UPDATE users SET email=?, password_hash=?, role=? WHERE id=?";
            try (Connection c = DataSourceManager.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getPasswordHash());
                ps.setString(3, user.getRole().name());
                ps.setLong(4, user.getId());
                ps.executeUpdate();
                return user;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setLogin(rs.getString("login"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRole(Role.valueOf(rs.getString("role")));
        return u;
    }
}