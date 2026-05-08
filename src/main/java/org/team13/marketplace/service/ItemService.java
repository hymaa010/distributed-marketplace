package org.team13.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team13.marketplace.dto.AddItemRequest;
import org.team13.marketplace.dto.UpdateItemRequest;
import org.team13.marketplace.exception.MarketplaceException;
import org.team13.marketplace.model.Item;
import org.team13.marketplace.model.ItemStatus;
import org.team13.marketplace.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Item addItem(String ownerId, AddItemRequest req) {
        Item item = Item.builder()
                .name(req.getName())
                .description(req.getDescription())
                .brand(req.getBrand())
                .price(req.getPrice())
                .quantity(req.getQuantity())
                .ownerId(ownerId)
                .build();

        return itemRepository.save(item);
    }


    public Item editItem(String itemId, String userId, UpdateItemRequest req) {
        Item item = getItemById(itemId);
        verifyOwnership(item, userId);

        if (item.getStatus() == ItemStatus.SOLD)
            throw new MarketplaceException("Cannot edit a sold item");

        if (req.getName() != null) item.setName(req.getName());
        if (req.getDescription() != null) item.setDescription(req.getDescription());
        if (req.getBrand() != null) item.setBrand(req.getBrand());
        if (req.getPrice() != null) item.setPrice(req.getPrice());
        if (req.getQuantity() != null) item.setQuantity(req.getQuantity());

        return itemRepository.save(item);
    }

    public void removeItem(String itemId, String userId) {
        Item item = getItemById(itemId);
        verifyOwnership(item, userId);

        if (item.getStatus() == ItemStatus.SOLD)
            throw new MarketplaceException("Cannot remove a sold item");

        item.setStatus(ItemStatus.UNLISTED);
        itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public List<Item> getAllAvailableItems() {
        return itemRepository.findByStatus(ItemStatus.AVAILABLE);
    }

    public Item getItemById(String itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new MarketplaceException("Item not found: " + itemId));
    }

    private void verifyOwnership(Item item, String userId) {
        if (!item.getOwnerId().equals(userId))
            throw new MarketplaceException("You do not own this item");
    }

    public List<Item> searchItems(String query) {
        return itemRepository.searchByNameOrBrand(query);
    }

}
