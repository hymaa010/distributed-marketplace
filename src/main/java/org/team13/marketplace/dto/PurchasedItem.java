package org.team13.marketplace.dto;

import lombok.Data;
import org.team13.marketplace.model.Item;

@Data
public class PurchasedItem {
    private String id;
    private String name;
    private String brand;
    private String description;
    private Double price;

    public PurchasedItem(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.brand = item.getBrand();
        this.description = item.getDescription();
        this.price = item.getPrice();
    }
}
