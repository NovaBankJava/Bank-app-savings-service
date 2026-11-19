package org.example.bankappsavingsservice.savings.domain.ports.in;

import org.example.bankappsavingsservice.savings.domain.model.Savings;

import java.math.BigDecimal;

public interface CreateSavingsUseCase {

    Savings create(String userId, String description, BigDecimal targetAmount);

}
