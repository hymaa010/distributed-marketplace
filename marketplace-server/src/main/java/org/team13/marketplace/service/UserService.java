package org.team13.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.team13.marketplace.dto.AccountInfoResponse;
import org.team13.marketplace.dto.ItemDto;
import org.team13.marketplace.dto.LoginRequest;
import org.team13.marketplace.model.PurchasedItem;
import org.team13.marketplace.dto.RegisterRequest;
import org.team13.marketplace.exception.MarketplaceException;
import org.team13.marketplace.model.Item;
import org.team13.marketplace.model.Transaction;
import org.team13.marketplace.model.User;
import org.team13.marketplace.repository.ItemRepository;
import org.team13.marketplace.repository.TransactionRepository;
import org.team13.marketplace.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final TransactionRepository transactionRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername()))
            throw new MarketplaceException("Username already taken");
        if (userRepository.existsByUsername(req.getUsername()))
            throw new MarketplaceException("Email already registered");

        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public User login(LoginRequest req) {
        User user = userRepository
                .findByUsername(req.getUsername())
                .orElseThrow(() -> new MarketplaceException("Invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash()))
            throw new MarketplaceException("Invalid credentials");

        return user;
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new MarketplaceException("User not found"));
    }

    public User deposit(String userId, double amount) {
        if (amount <= 0)
            throw new MarketplaceException("Deposit amount must be positive");

        User user = getUserById(userId);
        user.setBalance(user.getBalance() + amount);
        return userRepository.save(user);
    }


    public AccountInfoResponse getAccountInfo(String userId) {
        User user = getUserById(userId);

        List<ItemDto> ownedItems = itemRepository.findByOwnerId(userId).stream().map(UserService::itemToDto).toList();

        List<Transaction> buyerTransactions = transactionRepository.findByBuyerId(userId);
        List<ItemDto> boughtItems = buyerTransactions.stream().map(UserService::purchasedItemToDto).toList();

        List<Transaction> sellerTransactions = transactionRepository.findBySellerId(userId);
        List<ItemDto> soldItems = sellerTransactions.stream().map(UserService::purchasedItemToDto).toList();

        return AccountInfoResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .balance(user.getBalance())
                .ownedItems(ownedItems)
                .soldItems(soldItems)
                .purchasedItems(boughtItems)
                .build();
    }

    private static ItemDto itemToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .brand(item.getBrand())
                .description(item.getDescription())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .status(item.getStatus().name())
                .ownerId(item.getOwnerId())
                .build();
    }

    private static ItemDto purchasedItemToDto(Transaction t) {
        return ItemDto.builder()
                .id(t.getPurchasedItem().getId())
                .name(t.getPurchasedItem().getName())
                .brand(t.getPurchasedItem().getBrand())
                .description(t.getPurchasedItem().getDescription())
                .price(t.getPurchasedItem().getPrice())
                .quantity(t.getQuantity())
                .build();
    }

}
