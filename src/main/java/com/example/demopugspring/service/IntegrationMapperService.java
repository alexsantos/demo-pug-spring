package com.example.demopugspring.service;

import java.util.List;

import com.example.demopugspring.model.Integration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demopugspring.model.IntegrationMapper;
import com.example.demopugspring.repository.IntegrationMapperRepository;

@Service
public class IntegrationMapperService {

	@Autowired
	private IntegrationMapperRepository integrationMapperRepository;

	public List<IntegrationMapper> retrieveIntegrationMappersFromIntegration(Integration integration) {
		return integrationMapperRepository.findByIntegration_IdOrderByOrderIndex(integration.getId());
	}

	public List<IntegrationMapper> retrieveActiveIntegrationMappersFromIntegration(Integration integration){
		return integrationMapperRepository.findByIntegration_IdAndActiveTrueOrderByOrderIndex(integration.getId());
	}

	public void save(Integration integration, List<IntegrationMapper> integrationMappers) {
		integrationMapperRepository.deleteAllByIntegration_Id(integration.getId());
		for(IntegrationMapper intMapper : integrationMappers){
			integrationMapperRepository.save(intMapper);
		}
	}
}
