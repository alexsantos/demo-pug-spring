package com.example.demopugspring.model;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;
import java.util.List;

@Entity
public class Mapper {

    public enum Category {
        TEXT("text"),
        FIELD("field"),
        TRANSCODING("transcoding");

        private final String value;

        Category(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    private List<String> key;
    private String value;
    private Mapper.Category type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getKey() {
        return key;
    }

    public void setKey(List<String> key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Mapper.Category getType() {
        return type;
    }

    public void setType(Mapper.Category type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Mapper{" +
                "id=" + id +
                ", key=" + key +
                ", value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
