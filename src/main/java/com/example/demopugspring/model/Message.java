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
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_code_event",columnNames = {"code", "event"})})
public class Message {
    @Id
    @GeneratedValue
    private Long id;
    private String code;
    private String event;


    public Message(String code, String event) {
        this.code = code;
        this.event = event;
    }
}
