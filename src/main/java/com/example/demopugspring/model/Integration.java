package com.example.demopugspring.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private Message resultMessage;

	public Integration(Message message, Application sendingApp, Application receivingApp, List<Mapper> mappers) {
		this.message = message;
		this.sendingApp = sendingApp;
		this.receivingApp = receivingApp;
	}
}
