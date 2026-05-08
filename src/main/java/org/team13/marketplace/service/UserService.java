package org.team13.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.team13.marketplace.model.User;
import org.team13.marketplace.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User register(String username, String password) {
        User user = User.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(password))
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public Double getBalance(String userId) {
        return userRepository.findById(userId)
                .map(User::getBalance)
                .orElse(0.0);
    }
}
