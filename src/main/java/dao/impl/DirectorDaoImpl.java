package dao.impl;


import dao.DirectorDao;
import entity.Director;
import util.DataSourceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DirectorDaoImpl implements DirectorDao {
    @Override
    public Optional<Director> findById(Long id) {
        String sql = "SELECT id, name FROM directors WHERE id = ?";
        try (Connection c = DataSourceManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(new Director(rs.getLong("id"), rs.getString("name")));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Director> findByName(String name) {
        String sql = "SELECT id, name FROM directors WHERE name = ?";
        try (Connection c = DataSourceManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(new Director(rs.getLong("id"), rs.getString("name")));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Director> findAll() {
        String sql = "SELECT id, name FROM directors ORDER BY name";
        try (Connection c = DataSourceManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Director> list = new ArrayList<>();
            while (rs.next()) list.add(new Director(rs.getLong("id"), rs.getString("name")));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Director save(Director director) {
        if (director.getId() == null) {
            String sql = "INSERT INTO directors(name) VALUES(?) RETURNING id";
            try (Connection c = DataSourceManager.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, director.getName());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) director.setId(rs.getLong(1));
                }
                return director;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String sql = "UPDATE directors SET name = ? WHERE id = ?";
            try (Connection c = DataSourceManager.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, director.getName());
                ps.setLong(2, director.getId());
                ps.executeUpdate();
                return director;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}