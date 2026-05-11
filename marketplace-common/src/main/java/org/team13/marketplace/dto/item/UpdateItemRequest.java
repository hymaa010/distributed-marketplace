package org.team13.marketplace.dto.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.team13.marketplace.model.ItemStatus;

@Data
public class UpdateItemRequest {

    // All fields are optional, null means no change
    private String name;
    private String description;
    private String brand;
    private ItemStatus status;

    @Positive
    private Double price;

    @Min(0)
    private Integer quantity;
}