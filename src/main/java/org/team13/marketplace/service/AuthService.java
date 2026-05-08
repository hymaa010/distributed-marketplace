package org.team13.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.team13.marketplace.model.User;
import org.team13.marketplace.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public String login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPasswordHash())) {
            // Generate a random session token
            String token = UUID.randomUUID().toString();
            user.get().setSessionToken(token);
            userRepository.save(user.get());
            return token;
        }
        return "AUTH_FAILED";
    }

    public boolean isValid(String token) {
        return userRepository.existsBySessionToken(token);
    }

    public Optional<User> getUserByToken(String token) {
        return userRepository.findBySessionToken(token);
    }
}
