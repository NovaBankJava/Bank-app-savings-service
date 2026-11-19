package org.example.bankappsavingsservice.savings.application.usecases;

import org.example.bankappsavingsservice.savings.domain.exception.SavingsNotFoundException;
import org.example.bankappsavingsservice.savings.domain.exception.SavingsWithBalanceException;
import org.example.bankappsavingsservice.savings.domain.model.Savings;
import org.example.bankappsavingsservice.savings.domain.ports.in.CreateSavingsUseCase;
import org.example.bankappsavingsservice.savings.domain.ports.in.DeleteSavingsUseCase;
import org.example.bankappsavingsservice.savings.domain.ports.in.GetSavingsUseCase;
import org.example.bankappsavingsservice.savings.domain.ports.in.UpdateSavingsUseCase;
import org.example.bankappsavingsservice.savings.domain.ports.out.SavingsRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service

public class SavingsService implements CreateSavingsUseCase, GetSavingsUseCase, DeleteSavingsUseCase, UpdateSavingsUseCase {

    private final SavingsRepositoryPort savingsRepositoryPort;

    public SavingsService(SavingsRepositoryPort savingsRepositoryPort) {
        this.savingsRepositoryPort = savingsRepositoryPort;
    }

    @Override
    public Savings create(String userId, String description, BigDecimal targetAmount) {
        Savings savings = Savings.create(
                userId,
                description,
                targetAmount
        );

        return savingsRepositoryPort.save(savings);
    }

    @Override
    public List<Savings> findByUserId(String userId){
        return savingsRepositoryPort.findByUserId(userId);

    }

    @Override
    public Savings update(String savingsId, String description, BigDecimal targetAmount){
        Savings savings = savingsRepositoryPort.findById(savingsId)
                .orElseThrow(() -> new SavingsNotFoundException(savingsId));

        savings.update(description, targetAmount);

        return savingsRepositoryPort.save(savings);
    }

    @Override
    public void delete(String savingsId){

       Savings savingsExists = savingsRepositoryPort.findById(savingsId)
               .orElseThrow(() -> new SavingsNotFoundException(savingsId));

       if (savingsExists.getBalance().compareTo(BigDecimal.ZERO) > 0) {
           throw new SavingsWithBalanceException("Savings with balance cannot be deleted: " + savingsId);
       }

       savingsRepositoryPort.deleteById(savingsId);

    }

}