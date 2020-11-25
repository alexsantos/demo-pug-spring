package com.example.demopugspring.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IntegrationMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Integration integration;
	@ManyToOne
	private Mapper mapper;
	private Integer orderIndex;
	private boolean active;

	public IntegrationMapper(Integration integration, Mapper mapper, Integer orderIndex, boolean active) {
		this.integration = integration;
		this.mapper = mapper;
		this.orderIndex = orderIndex;
		this.active = active;
	}
}
