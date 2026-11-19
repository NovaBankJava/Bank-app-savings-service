package org.example.bankappsavingsservice.savings.application.usecases;

import org.example.bankappsavingsservice.savings.domain.exception.SavingsNotFoundException;
import org.example.bankappsavingsservice.savings.domain.exception.SavingsWithBalanceException;
import org.example.bankappsavingsservice.savings.domain.model.Savings;
import org.example.bankappsavingsservice.savings.domain.ports.out.SavingsRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SavingsServiceTest {

    @Mock
    private SavingsRepositoryPort repository;

    @InjectMocks
    private SavingsService service;


    @Test
    void shouldThrowNotFoundWhenSavingsDoesNotExist() {
        when(repository.findById("abc")).thenReturn(Optional.empty());

        assertThrows(SavingsNotFoundException.class, () -> service.delete("abc"));

        verify(repository, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenSavingsHasBalance() {
        Savings savings = new Savings(
                "test123",
                "user-1",
                "Travel",
                new BigDecimal("100"),
                new BigDecimal("1000"),
                LocalDateTime.now()
        );

        when(repository.findById("test123")).thenReturn(Optional.of(savings));

        assertThrows(SavingsWithBalanceException.class, () -> service.delete("test123"));

        verify(repository, never()).deleteById(any());
    }

    @Test
    void shouldDeleteWhenSavingsHasNoBalance(){

        Savings savings = new Savings(
                "test123",
                "user-1",
                "Travel",
                new BigDecimal("0"),
                new BigDecimal("1000"),
                LocalDateTime.now()
        );

        when(repository.findById("test123")).thenReturn(Optional.of(savings));

        service.delete("test123");

        verify(repository).deleteById("test123");

    }
}