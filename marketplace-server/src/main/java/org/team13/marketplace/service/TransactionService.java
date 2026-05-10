package org.team13.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team13.marketplace.model.PurchasedItem;
import org.team13.marketplace.model.Item;
import org.team13.marketplace.model.ItemStatus;
import org.team13.marketplace.model.Transaction;
import org.team13.marketplace.model.User;
import org.team13.marketplace.repository.ItemRepository;
import org.team13.marketplace.repository.TransactionRepository;
import org.team13.marketplace.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Transaction purchaseItem(String buyerId, String itemId, int quantity) {
        Item item = itemRepository.findById(itemId).orElseThrow();

        double totalCost = item.getPrice() * quantity;
        User buyer = userRepository.findById(item.getOwnerId()).orElseThrow();
        User seller = userRepository.findById(item.getOwnerId()).orElseThrow();

        // Update balances
        buyer.setBalance(buyer.getBalance() - totalCost);
        seller.setBalance(seller.getBalance() + totalCost);
        userRepository.save(buyer);
        userRepository.save(seller);

        // Update new Item owner, quantity
        int remaining = item.getQuantity() - quantity;
        if (remaining == 0) {
            item.setStatus(ItemStatus.SOLD);
            item.setOwnerId(buyer.getId());
        } else {
            item.setQuantity(remaining);
            Item soldItem = item.toBuilder()
                    .id(null)
                    .quantity(quantity)
                    .status(ItemStatus.SOLD)
                    .ownerId(buyerId)
                    .build();

            itemRepository.save(soldItem);
        }

        itemRepository.save(item);

        // Create Transaction Record
        Transaction tx = Transaction.builder()
                .buyerId(buyer.getId())
                .sellerId(seller.getId())
                .purchasedItem(new PurchasedItem(item))
                .quantity(quantity)
                .createdAt(LocalDateTime.now())
                .build();

        return transactionRepository.save(tx);
    }

    public List<Transaction> getPurchaseHistory(String userId) {
        return transactionRepository.findByBuyerId(userId);
    }

    public List<Transaction> getSalesHistory(String userId) {
        return transactionRepository.findBySellerId(userId);
    }
}
