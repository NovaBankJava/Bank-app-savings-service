package org.example.bankappsavingsservice.infra.adapter.in.dto;

import java.math.BigDecimal;

public record CreateSavingsRequest(String userId, String description, BigDecimal targetAmount) {
}
