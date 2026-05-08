package org.team13.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team13.marketplace.model.Item;
import org.team13.marketplace.model.User;
import org.team13.marketplace.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final AuthService authService;

    public Item addItem(String token, Item item) {
        Optional<User> buyerAuth = authService.getUserByToken(token);
        if (buyerAuth.isEmpty()) return null;
        return itemRepository.save(item);
    }

    public List<Item> getAllItems() { return itemRepository.findAll(); }

    public List<Item> searchItems(String query) {
        return itemRepository.searchByNameOrBrand(query);
    }

    public void deleteItem(String id) { itemRepository.deleteById(id); }
}
