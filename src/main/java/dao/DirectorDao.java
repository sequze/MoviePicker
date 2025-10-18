package dao;

import entity.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorDao {
    Optional<Director> findById(Long id);
    Optional<Director> findByName(String name);
    List<Director> findAll();
    Director save(Director director);
}
