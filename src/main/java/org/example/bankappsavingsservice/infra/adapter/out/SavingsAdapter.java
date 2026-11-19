package org.example.bankappsavingsservice.infra.adapter.out;


import org.example.bankappsavingsservice.infra.adapter.out.mapper.SavingsOutMapper;
import org.example.bankappsavingsservice.infra.repository.SavingsRepository;
import org.example.bankappsavingsservice.infra.repository.entity.SavingsEntity;
import org.example.bankappsavingsservice.savings.domain.model.Savings;
import org.example.bankappsavingsservice.savings.domain.ports.out.SavingsRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SavingsAdapter implements SavingsRepositoryPort{

    private final SavingsRepository jpaRepository;
    private final SavingsOutMapper mapper;

    public SavingsAdapter(
            SavingsRepository jpaRepository,
            SavingsOutMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;

    }

    @Override
    public Savings save(Savings savings) {
        SavingsEntity entity = mapper.toJpa(savings);

        SavingsEntity savedEntity = jpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Savings> findById(String savingsId){
        return jpaRepository.findById(savingsId).map(mapper::toDomain);
    }

    @Override
    public List<Savings> findByUserId(String userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }



    @Override
    public void deleteById(String savingsId){
        jpaRepository.deleteById(savingsId);
    }
}
