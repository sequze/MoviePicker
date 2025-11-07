package service.impl;

import dao.UserDao;
import dto.UserDTO;
import entity.Role;
import service.UserService;
import entity.User;
import util.PasswordUtil;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<UserDTO> login(String login, String password) {
        Optional<User> res = userDao.findByLogin(login)
                .filter(u -> PasswordUtil.verify(password, u.getPasswordHash()));
        return res.map(UserDTO::new);
    }

    @Override
    public UserDTO register(String login, String email, String password) {
        if (userDao.existsByLogin(login)) {
            throw new IllegalArgumentException("Login already exists");
        }
        User u = new User();
        u.setLogin(login);
        u.setEmail(email);
        u.setPasswordHash(PasswordUtil.hash(password));
        u.setRole(Role.user);
        return new UserDTO(userDao.save(u));
    }

    @Override
    public boolean loginExists(String login) {
        return userDao.existsByLogin(login);
    }
}