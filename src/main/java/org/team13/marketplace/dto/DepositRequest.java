package org.team13.marketplace.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DepositRequest {

    @Positive(message = "Deposit amount must be positive")
    private double amount;
}
