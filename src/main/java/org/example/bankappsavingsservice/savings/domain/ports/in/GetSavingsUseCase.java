package org.example.bankappsavingsservice.savings.domain.ports.in;

import org.example.bankappsavingsservice.savings.domain.model.Savings;

import java.util.List;

public interface GetSavingsUseCase {

        List<Savings> findByUserId(String userId);
}
