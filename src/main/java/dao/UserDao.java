package dao;

import entity.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String login);
    User save(User user); // returns user with id
}