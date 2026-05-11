package org.team13.marketplace.mapper;

import org.team13.marketplace.dto.item.ItemDto;
import org.team13.marketplace.model.Item;
import org.team13.marketplace.model.PurchasedItem;

public class ItemMapper {

    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .brand(item.getBrand())
                .price(item.getPrice())
                .status(item.getStatus().name())
                .ownerId(item.getOwnerId())
                .build();
    }

    public static ItemDto toDto(PurchasedItem p) {
        return ItemDto.builder()
                .id(p.getId())
                .name(p.getName())
                .brand(p.getBrand())
                .description(p.getDescription())
                .price(p.getPrice())
                .build();
    }
}
