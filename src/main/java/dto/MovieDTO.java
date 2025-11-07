package dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class MovieDTO {
    private Long id;
    private String name;
    private String description;
    private String director;
    private String genre;
    private Double rating;
    private String posterUrl;
    private Integer year;

    public MovieDTO(Map<String, Object> map) {
        this.id = (Long) map.get("id");
        this.name = (String) map.get("name");
        this.description = (String) map.get("description");
        this.director = (String) map.get("director");
        this.genre = (String) map.get("genre");
        this.rating = (Double) map.get("rating");
        this.posterUrl = (String) map.get("posterUrl");
        this.year = (Integer) map.get("year");
    }
}
