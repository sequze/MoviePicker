package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor@Getter@Setter
public class UserWatched {
    private Long userId;
    private Long movieId;
}
