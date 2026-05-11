package org.team13.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team13.marketplace.dto.item.AddItemRequest;
import org.team13.marketplace.dto.item.ItemDto;
import org.team13.marketplace.dto.item.UpdateItemRequest;
import org.team13.marketplace.exception.MarketplaceException;
import org.team13.marketplace.mapper.ItemMapper;
import org.team13.marketplace.model.Item;
import org.team13.marketplace.model.ItemStatus;
import org.team13.marketplace.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemDto addItem(String ownerId, AddItemRequest req) {
        Item item = itemRepository.save(Item.builder()
                .name(req.getName())
                .brand(req.getBrand())
                .price(req.getPrice())
                .quantity(req.getQuantity())
                .description(req.getDescription())
                .ownerId(ownerId)
                .build());

        return ItemMapper.toDto(item);
    }

    public ItemDto editItem(String itemId, String userId, UpdateItemRequest req) {
        Item item = getItemById(itemId);
        verifyOwnership(item, userId);

        if (req.getName() != null) item.setName(req.getName());
        if (req.getBrand() != null) item.setBrand(req.getBrand());
        if (req.getPrice() != null) item.setPrice(req.getPrice());
        if (req.getStatus() != null) item.setStatus(req.getStatus());
        if (req.getQuantity() != null) item.setQuantity(req.getQuantity());
        if (req.getDescription() != null) item.setDescription(req.getDescription());
        itemRepository.save(item);

        return ItemMapper.toDto(item);
    }

    public void removeItem(String itemId, String userId) {
        Item item = getItemById(itemId);
        verifyOwnership(item, userId);

        item.setStatus(ItemStatus.UNLISTED);
        itemRepository.save(item);
    }

    public List<ItemDto> getAllAvailableItems() {
        return itemRepository.findByStatus(ItemStatus.AVAILABLE).stream().map(ItemMapper::toDto).toList();
    }

    private Item getItemById(String itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new MarketplaceException("Item not found: " + itemId));
    }

    private void verifyOwnership(Item item, String userId) {
        if (!item.getOwnerId().equals(userId))
            throw new MarketplaceException("You do not own this item");
    }

    public List<ItemDto> searchItems(String query) {
        return itemRepository.searchByNameOrBrand(query).stream().map(ItemMapper::toDto).toList();
    }

}
