package com.example.demopugspring.service;

import com.example.demopugspring.model.Mapper;
import com.example.demopugspring.repository.MapperRepository;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MapperService {

	@Autowired
	private MapperRepository mapperRepository;

	public List<Mapper> findAll() {
		List<Mapper> maps = new ArrayList<>();
		mapperRepository.findAll().forEach(maps::add);

		return maps;
	}

	public List<Mapper> findALlByIdNotIn(List<Long> mapperIDs){
		List<Mapper> maps = new ArrayList<>();
		if(mapperIDs.isEmpty()){
			mapperRepository.findAll().forEach(maps::add);
		}else{
			mapperRepository.findAllByIdNotIn(mapperIDs).forEach(maps::add);
		}
		return maps;
	}

	public Mapper findById(Long id) {
		return mapperRepository.findById(id).orElseThrow();
	}

	public Mapper save(Mapper mapper) {
		List<String> temp = mapper.getKey();
		temp.removeAll(Collections.singleton(""));
		mapper.setKey(temp);
		return mapperRepository.save(mapper);
	}
}
