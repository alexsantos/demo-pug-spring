package com.example.demopugspring.repository;

import com.example.demopugspring.model.Application;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository extends CrudRepository<Application, Long> {
}
