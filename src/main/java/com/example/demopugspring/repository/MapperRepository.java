package com.example.demopugspring.repository;

import com.example.demopugspring.model.Mapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapperRepository extends CrudRepository<Mapper, Long> {
     List<Mapper> findAllByIdNotIn(List<Long> mappersID);
}
