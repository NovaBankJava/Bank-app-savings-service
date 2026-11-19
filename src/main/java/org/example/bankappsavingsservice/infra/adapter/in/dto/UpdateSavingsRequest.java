package org.example.bankappsavingsservice.infra.adapter.in.dto;

import java.math.BigDecimal;

public record UpdateSavingsRequest(String description, BigDecimal targetAmount) {
}
