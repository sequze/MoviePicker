package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor@AllArgsConstructor@Setter@Getter
public class User {
    private Long id;
    private String login;
    private String email;
    private String passwordHash;
    private Role role = Role.user;
    private LocalDateTime createdAt;
}