package org.team13.marketplace.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team13.marketplace.dto.item.ItemDto;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private String id;
    private String buyerId;
    private String sellerId;
    private ItemDto purchasedItem;
    private Integer quantity;
    private LocalDateTime createdAt;
}
