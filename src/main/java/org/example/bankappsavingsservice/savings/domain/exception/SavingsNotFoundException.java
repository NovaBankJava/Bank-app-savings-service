package org.example.bankappsavingsservice.savings.domain.exception;

public class SavingsNotFoundException extends RuntimeException{

    public SavingsNotFoundException(String savingsId){
        super("Savings not found: " + savingsId);
    }
}
