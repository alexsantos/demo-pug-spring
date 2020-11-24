package com.example.demopugspring.service;

import com.example.demopugspring.model.Application;
import com.example.demopugspring.model.Integration;
import com.example.demopugspring.model.Mapper;
import com.example.demopugspring.model.Message;
import com.example.demopugspring.repository.IntegrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IntegrationService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IntegrationRepository integrationRepository;

	@Autowired
	private MapperService mapperService;

	public List<Integration> findAll() {
		List<Integration> integrations = new ArrayList<>();
		integrationRepository.findAll().forEach(integrations::add);
		return integrations;
	}

	public Integration findById(Long id) {
		return integrationRepository.findById(id).orElseThrow();
	}

	public Integration save(Integration integration) {
		return integrationRepository.save(integration);
	}

	public Integration updateMappers(Long id, List<Long> mapperIds) {
		Integration integration = findById(id);
		List<Mapper> mappers = new ArrayList<>();
		for (Long item : mapperIds) {
			mappers.add(mapperService.findById(item));
		}
		logger.info(mappers.toString());
		integration.setMappers(mappers);
		return save(integration);
	}

	public Integration findByMessageAndApplications(Message message, Application sending, Application receiving) {
		return integrationRepository.findByMessageAndSendingAppAndReceivingApp(message, sending, receiving);
	}
}
