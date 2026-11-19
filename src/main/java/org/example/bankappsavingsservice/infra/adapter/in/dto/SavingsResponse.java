package org.example.bankappsavingsservice.infra.adapter.in.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SavingsResponse(String id, String userId, String description, BigDecimal targetAmount, BigDecimal balance, LocalDateTime createdAt) {
}
