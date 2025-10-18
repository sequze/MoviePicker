package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor@Getter@Setter
public class Movie {
    private Long id;
    private String name;
    private String description;
    private Integer directorId;
    private Integer genreId;
    private Double rating;
    private String posterUrl;
    private Integer year;
}
