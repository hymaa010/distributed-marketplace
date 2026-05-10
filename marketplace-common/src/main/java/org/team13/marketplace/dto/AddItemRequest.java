package org.team13.marketplace.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddItemRequest {

    @NotBlank(message = "Item name is required")
    private String name;

    private String description;

    private String brand;

    @Positive(message = "Price must be positive")
    private double price;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
