package com.example.demopugspring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Application {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String code;
    private String name;

    public Application(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
