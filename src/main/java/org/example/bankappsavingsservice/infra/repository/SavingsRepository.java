package org.example.bankappsavingsservice.infra.repository;


import org.example.bankappsavingsservice.infra.repository.entity.SavingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SavingsRepository extends JpaRepository<SavingsEntity, String> {

    List<SavingsEntity> findByUserId(String userId);
    
}
