package service.impl;

import dao.DirectorDao;
import entity.Director;
import service.DirectorService;

import java.util.List;
import java.util.Optional;

public class DirectorServiceImpl implements DirectorService {
    private final DirectorDao directorDao;

    public DirectorServiceImpl(DirectorDao directorDao) {
        this.directorDao = directorDao;
    }

    @Override
    public void deleteDirector(Long directorId) {
        directorDao.deleteById(directorId);
    }

    @Override
    public List<Director> getAllDirectors() {
        return directorDao.findAll();
    }

    @Override
    public Director createDirector(String name) {
        Director director = new Director(null, name);
        directorDao.save(director);
        return director;
    }

    @Override
    public void updateDirector(Long directorId, String newName) {
        directorDao.save(new Director(directorId, newName));
    }

    @Override
    public Optional<Director> getDirectorById(Long directorId) {
        return directorDao.findById(directorId);
    }
}
