package com.example.demopugspring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Mapper {

    public enum Category {
        TEXT("text"),
        FIELD("field"),
        TRANSCODING("transcoding");

        private final String value;

        Category(String value) {
            this.value = value;
        }

        //@JsonValue
        public String getValue() {
            return value;
        }
    }

    @Id
    @GeneratedValue
    private Long id;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> key;
    private String value;
    private Mapper.Category category;

    public Mapper(List<String> key, String value, Mapper.Category category) {
        this.key = key;
        this.value = value;
        this.category = category;
    }

}
