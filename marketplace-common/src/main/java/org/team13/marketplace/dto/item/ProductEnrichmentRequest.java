package org.team13.marketplace.dto.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductEnrichmentRequest {
    @NotBlank(message = "Item name is required")
    private String name;
    private String brand;
}
