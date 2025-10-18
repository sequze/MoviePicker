package dao.impl;

import dao.MovieDao;
import entity.Movie;
import util.DataSourceManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDaoImpl implements MovieDao {
    @Override
    public List<Movie> findAll() {
        String sql = "SELECT id, name, description, director_id, genre_id, rating, poster_url, year FROM movies ORDER BY id DESC";
        try (Connection c = DataSourceManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Movie> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Movie> findById(Long id) {
        String sql = "SELECT id, name, description, director_id, genre_id, rating, poster_url, year FROM movies WHERE id = ?";
        try (Connection c = DataSourceManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Movie save(Movie m) {
        if (m.getId() == null) {
            String sql = "INSERT INTO movies(name, description, director_id, genre_id, rating, poster_url, year) VALUES(?,?,?,?,?,?,?)";
            try (Connection c = DataSourceManager.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                bind(ps, m);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) m.setId(keys.getLong(1));
                }
                return m;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String sql = "UPDATE movies SET name=?, description=?, director_id=?, genre_id=?, rating=?, poster_url=?, year=? WHERE id=?";
            try (Connection c = DataSourceManager.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                bind(ps, m);
                ps.setLong(8, m.getId());
                ps.executeUpdate();
                return m;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM movies WHERE id = ?";
        try (Connection c = DataSourceManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Object> generateSqlExpressionWithFilters(String genre, Double minRating, String director) {
        List<Object> sql_with_params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, name, description, director, genre, rating, poster_url, trailer_url, year FROM movies WHERE 1=1");
        if (genre != null && !genre.isEmpty()) { sql.append(" AND genre = ?"); sql_with_params.add(genre); }
        if (minRating != null) { sql.append(" AND rating >= ?"); sql_with_params.add(minRating); }
        if (director != null && !director.isEmpty()) { sql.append(" AND director = ?"); sql_with_params.add(director); }
        sql_with_params.addFirst(sql);
        return sql_with_params;
    }
    @Override
    public List<Movie> findByFilters(String genre, Double minRating, String director) {
        List<Object> sql_with_params =  generateSqlExpressionWithFilters(genre, minRating, director);
        StringBuilder sql = (StringBuilder) sql_with_params.getFirst();
        List<Object> params = sql_with_params.subList(0, sql_with_params.size());
        sql.append(" ORDER BY id DESC");
        try (Connection c = DataSourceManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            List<Movie> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Movie> findRandomByFilters(String genre, Double minRating, String director) {
        List<Object> sql_with_params =  generateSqlExpressionWithFilters(genre, minRating, director);
        StringBuilder sql = (StringBuilder) sql_with_params.getFirst();
        List<Object> params = sql_with_params.subList(0, sql_with_params.size());
        sql.append(" ORDER BY RAND() LIMIT 1");
        try (Connection c = DataSourceManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void bind(PreparedStatement ps, Movie m) throws SQLException {
        ps.setString(1, m.getName());
        ps.setString(2, m.getDescription());
        ps.setInt(3, m.getDirectorId());
        ps.setInt(4, m.getGenreId());
        if (m.getRating() != null) ps.setDouble(5, m.getRating()); else ps.setNull(5, Types.DOUBLE);
        ps.setString(6, m.getPosterUrl());
        ps.setInt(7, m.getYear());
    }

    private Movie map(ResultSet rs) throws SQLException {
        Movie m = new Movie();
        m.setId(rs.getLong("id"));
        m.setName(rs.getString("name"));
        m.setDescription(rs.getString("description"));
        m.setDirectorId(rs.getInt("director_id"));
        m.setGenreId(rs.getInt("genre_id"));
        double rating = rs.getDouble("rating");
        if (!rs.wasNull()) m.setRating(rating);
        m.setPosterUrl(rs.getString("poster_url"));
        m.setYear(rs.getInt("year"));
        return m;
    }
}
