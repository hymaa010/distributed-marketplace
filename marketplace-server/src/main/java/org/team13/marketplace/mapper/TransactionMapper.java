package org.team13.marketplace.mapper;

import org.team13.marketplace.dto.transaction.TransactionDto;
import org.team13.marketplace.model.Transaction;

public class TransactionMapper {
    public static TransactionDto toDto(Transaction t) {
        return TransactionDto.builder()
                .id(t.getId())
                .quantity(t.getQuantity())
                .buyerId(t.getBuyerId())
                .sellerId(t.getSellerId())
                .purchasedItem(ItemMapper.toDto(t.getPurchasedItem()))
                .createdAt(t.getCreatedAt())
                .build();
    }
}
