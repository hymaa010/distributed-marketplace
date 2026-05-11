package org.team13.marketplace.dto.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private String id;
    private String name;
    private String brand;
    private String description;
    private Double price;
    private Integer quantity;
    private String status;
    private String ownerId;
}
