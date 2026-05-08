package org.team13.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.team13.marketplace.dto.AccountInfoResponse;
import org.team13.marketplace.dto.LoginRequest;
import org.team13.marketplace.dto.PurchasedItem;
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

        List<Item> ownedItems = itemRepository.findByOwnerId(userId);

        List<Transaction> buyerTransactions = transactionRepository.findByBuyerId(userId);
        List<PurchasedItem> boughtItems = buyerTransactions.stream().map(Transaction::getPurchasedItem).toList();

        List<Transaction> sellerTransactions = transactionRepository.findBySellerId(userId);
        List<PurchasedItem> soldItems = sellerTransactions.stream().map(Transaction::getPurchasedItem).toList();

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

}
