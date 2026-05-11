package org.team13.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team13.marketplace.dto.transaction.TransactionDto;
import org.team13.marketplace.mapper.TransactionMapper;
import org.team13.marketplace.model.*;
import org.team13.marketplace.repository.ItemRepository;
import org.team13.marketplace.repository.TransactionRepository;
import org.team13.marketplace.repository.UserAccountRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserAccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public TransactionDto purchaseItem(String buyerId, String itemId, int quantity) {
        Item item = itemRepository.findById(itemId).orElseThrow();

        double totalCost = item.getPrice() * quantity;
        UserAccount buyer = accountRepository.findById(item.getOwnerId()).orElseThrow();
        UserAccount seller = accountRepository.findById(item.getOwnerId()).orElseThrow();

        // Update balances
        buyer.setBalance(buyer.getBalance() - totalCost);
        seller.setBalance(seller.getBalance() + totalCost);
        accountRepository.save(buyer);
        accountRepository.save(seller);

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
        Transaction tx = transactionRepository.save(Transaction.builder()
                .buyerId(buyer.getId())
                .sellerId(seller.getId())
                .purchasedItem(new PurchasedItem(item))
                .quantity(quantity)
                .createdAt(LocalDateTime.now())
                .build());

        return TransactionMapper.toDto(tx);
    }
}
