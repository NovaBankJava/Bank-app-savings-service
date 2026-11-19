package org.example.bankappsavingsservice.savings.domain.ports.in;

import org.example.bankappsavingsservice.savings.domain.model.Savings;

import java.math.BigDecimal;

public interface UpdateSavingsUseCase {

    Savings update(String savingsId, String description, BigDecimal targetAmount);
}
