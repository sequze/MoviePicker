package service;

import entity.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorService {
    void deleteDirector(Long directorId);
    List<Director> getAllDirectors();
    void updateDirector(Long directorId, String newName);
    Director createDirector(String name);
    Optional<Director> getDirectorById(Long directorId);
}
