package org.team13.marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.team13.marketplace.dto.item.ItemDto;
import org.team13.marketplace.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<ItemDto> getAllAvailable() {
        return itemService.getAllAvailableItems();
    }

//    @PostMapping
//    public Item create(@RequestBody Item item) {
//        return itemService.addItem(item);
//    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String q) {
        return itemService.searchItems(q);
    }
}
