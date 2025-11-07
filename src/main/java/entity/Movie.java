package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor@Getter@Setter@ToString
public class Movie {
    private Long id;
    private String name;
    private String description;
    private Long directorId;
    private Long genreId;
    private Double rating;
    private String posterUrl;
    private Integer year;
}
