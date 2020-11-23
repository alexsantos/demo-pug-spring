package com.example.demopugspring.repository;

import com.example.demopugspring.model.Application;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationRepository extends CrudRepository<Application, Long> {
	public Application findByCode(String code);

	public List<Application> findByOrderByCodeAsc();
}
