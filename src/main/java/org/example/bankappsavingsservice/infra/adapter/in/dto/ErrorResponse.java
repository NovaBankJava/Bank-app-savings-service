package org.example.bankappsavingsservice.infra.adapter.in.dto;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String message, LocalDateTime timestamp) {

}
