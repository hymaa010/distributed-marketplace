package org.team13.marketplace.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateItemRequest {

    // All fields are optional, null means no change
    private String name;
    private String description;
    private String brand;

    @Positive
    private Double price;

    @Min(0)
    private Integer quantity;
}