package org.example.bankappsavingsservice.infra.adapter.in.controller;

import org.example.bankappsavingsservice.infra.adapter.in.dto.CreateSavingsRequest;
import org.example.bankappsavingsservice.infra.adapter.in.dto.SavingsResponse;
import org.example.bankappsavingsservice.infra.adapter.in.dto.UpdateSavingsRequest;
import org.example.bankappsavingsservice.savings.domain.model.Savings;
import org.example.bankappsavingsservice.savings.domain.ports.in.CreateSavingsUseCase;
import org.example.bankappsavingsservice.savings.domain.ports.in.DeleteSavingsUseCase;
import org.example.bankappsavingsservice.savings.domain.ports.in.GetSavingsUseCase;
import org.example.bankappsavingsservice.savings.domain.ports.in.UpdateSavingsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/savings")
public class SavingsController {

    private final CreateSavingsUseCase createSavingsUseCase;
    private final GetSavingsUseCase getSavingsUseCase;
    private final UpdateSavingsUseCase updateSavingsUseCase;
    private final DeleteSavingsUseCase deleteSavingsUseCase;

    public SavingsController(CreateSavingsUseCase createSavingsUseCase, GetSavingsUseCase getSavingsUseCase, DeleteSavingsUseCase deleteSavingsUseCase, UpdateSavingsUseCase updateSavingsUseCase) {
        this.createSavingsUseCase = createSavingsUseCase;
        this.getSavingsUseCase = getSavingsUseCase;
        this.deleteSavingsUseCase = deleteSavingsUseCase;
        this.updateSavingsUseCase = updateSavingsUseCase;
    }

    @PostMapping
    public ResponseEntity<SavingsResponse> create(@RequestBody CreateSavingsRequest request) {

        Savings savings = createSavingsUseCase.create(
                request.userId(),
                request.description(),
                request.targetAmount()
        );

        SavingsResponse response = new SavingsResponse(
                savings.getId(),
                savings.getUserId(),
                savings.getDescription(),
                savings.getTargetAmount(),
                savings.getBalance(),
                savings.getCreatedAt()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SavingsResponse>> findByUserId(@PathVariable String userId){

        List<SavingsResponse> response = getSavingsUseCase
                .findByUserId(userId)
                .stream()
                .map(savings -> new SavingsResponse(
                        savings.getId(),
                        savings.getUserId(),
                        savings.getDescription(),
                        savings.getTargetAmount(),
                        savings.getBalance(),
                        savings.getCreatedAt()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{savingsId}")
    public ResponseEntity<SavingsResponse> update(@PathVariable String savingsId, @RequestBody UpdateSavingsRequest request){

        Savings savings = updateSavingsUseCase.update(
                savingsId,
                request.description(),
                request.targetAmount()
        );

        SavingsResponse response = new SavingsResponse(
                savings.getId(),
                savings.getUserId(),
                savings.getDescription(),
                savings.getTargetAmount(),
                savings.getBalance(),
                savings.getCreatedAt()
        );

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{savingsId}")
    public ResponseEntity<Void> delete(@PathVariable String savingsId){

        deleteSavingsUseCase.delete(savingsId);

        return ResponseEntity.noContent().build();
    }
}