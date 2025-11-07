package dao;

import entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Optional<Genre> findById(Long id);
    Optional<Genre> findByName(String name);
    List<Genre> findAll();
    void save(Genre genre);
    void deleteById(Long id);
}
