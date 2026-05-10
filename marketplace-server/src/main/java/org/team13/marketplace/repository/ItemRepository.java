package org.team13.marketplace.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.team13.marketplace.model.Item;
import org.team13.marketplace.model.ItemStatus;

import java.util.List;

public interface ItemRepository extends MongoRepository<Item, String> {
    @Query("{ $or: [ " +
            " { name: { $regex: ?0, $options: 'i' } }, " +
            " { brand: { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<Item> searchByNameOrBrand(String query);

    List<Item> findByOwnerId(String ownerId);

    List<Item> findByStatus(ItemStatus status);
}
