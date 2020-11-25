package com.example.demopugspring.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.example.demopugspring.model.IntegrationMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IntegrationMapperRepository extends CrudRepository<IntegrationMapper, Long> {
	 List<IntegrationMapper> findByIntegration_IdOrderByOrderIndex(Long integrationID);
	 List<IntegrationMapper> findByIntegration_IdAndActiveTrueOrderByOrderIndex(Long integrationID);
	 @Transactional
	 long deleteAllByIntegration_Id(Long integrationID);
}
