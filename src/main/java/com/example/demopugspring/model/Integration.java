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
@Table(indexes = {@Index(name = "idx_msg_apps", columnList = "message,sending_app,receiving_app")})
public class Integration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Message message;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Application sendingApp;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Application receivingApp;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Mapper> mappers;

    public Integration(Message message, Application sendingApp, Application receivingApp, List<Mapper> mappers) {
        this.message = message;
        this.sendingApp = sendingApp;
        this.receivingApp = receivingApp;
        this.mappers = mappers;
    }

    public boolean hasMapper(Long id) {
        for (Mapper mapper: this.mappers) {
            if (mapper.getId() == id) {
                return true;
            }
        }
        return false;
    }
}
