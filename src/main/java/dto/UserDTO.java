package dto;

import entity.Role;
import entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String login;
    private String email;
    private Role role = Role.user;

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
