package service;

import dto.UserDTO;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> login(String login, String password);
    UserDTO register(String login, String email, String password);
    boolean loginExists(String login);
}