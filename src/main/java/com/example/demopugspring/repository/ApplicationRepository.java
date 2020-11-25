package com.example.demopugspring.repository;

import com.example.demopugspring.model.Application;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {
	public Application findByCode(String code);

	public List<Application> findByOrderByCodeAsc();
}
