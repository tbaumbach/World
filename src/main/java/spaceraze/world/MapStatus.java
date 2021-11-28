package spaceraze.world;

import lombok.Getter;

@Getter
public enum MapStatus {

    DRAFT("DRAFT", "Draft"),
    PUBLISHED("PUBLISHED", "Published"),
    REPLACED("REPLACED", "Replaced");

    private String id;
    private String description;

    MapStatus(String id, String description){
        this.id = id;
        this.description = description;
    }
}
