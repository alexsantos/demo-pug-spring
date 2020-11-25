package com.example.demopugspring.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demopugspring.model.Application;
import com.example.demopugspring.model.Integration;
import com.example.demopugspring.model.IntegrationMapper;
import com.example.demopugspring.model.Mapper;
import com.example.demopugspring.model.Message;
import com.example.demopugspring.repository.IntegrationRepository;

@Service
public class IntegrationService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IntegrationRepository integrationRepository;

	@Autowired
	private MapperService mapperService;

	@Autowired
	private IntegrationMapperService integrationMapperService;

	public List<Integration> findAll() {
		List<Integration> integrations = new ArrayList<>();
		integrationRepository.findAll().forEach(integrations::add);
		return integrations;
	}

	public Integration findById(Long id) {
		return integrationRepository.findById(id).orElseThrow();
	}

	public Integration save(Integration integration, List<IntegrationMapper> mappers) {
		integrationMapperService.save(integration, mappers);
		return save(integration);
	}

	public Integration save(Integration integration) {
		return integrationRepository.save(integration);
	}

	public Integration updateMappers(Long id, List<Long> mapperIds, List<Long> mapperActive) {
		Integration integration = findById(id);
		List<IntegrationMapper> listInterMapper = new ArrayList<>();
		int orderIndex;
		for (int i = 0; i < mapperIds.size(); i++) {
			IntegrationMapper interMapper = new IntegrationMapper(integration, mapperService.findById(mapperIds.get(i)), i, mapperActive.contains(mapperIds.get(i)));
			listInterMapper.add(interMapper);
		}

		return save(integration, listInterMapper);
	}

	public Integration findByMessageAndApplications(Message message, Application sending, Application receiving) {
		return integrationRepository.findByMessageAndSendingAppAndReceivingApp(message, sending, receiving);
	}
}
