package org.example.bankappsavingsservice.infra.adapter.out.mapper;

import org.example.bankappsavingsservice.infra.repository.entity.SavingsEntity;
import org.example.bankappsavingsservice.savings.domain.model.Savings;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SavingsOutMapper {

    SavingsEntity toJpa(Savings savings);

    Savings toDomain(SavingsEntity entity);
}
