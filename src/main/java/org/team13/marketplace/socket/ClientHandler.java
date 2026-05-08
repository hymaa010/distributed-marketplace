package org.team13.marketplace.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.team13.marketplace.dto.*;
import org.team13.marketplace.model.User;
import org.team13.marketplace.service.ItemService;
import org.team13.marketplace.service.TransactionService;
import org.team13.marketplace.service.UserService;
import tools.jackson.databind.json.JsonMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientHandler {

    private final ItemService itemService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final JsonMapper mapper;

    private String authenticatedUserId = null;

    public void handleClient(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String input;
            while ((input = in.readLine()) != null) {
                SocketRequest req = mapper.readValue(input, SocketRequest.class);
                handleCommand(req, out);
                if ("DISCONNECT".equals(req.getCommand())) break;
            }
        } catch (Exception e) {
            log.error("Client handler error", e);
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void handleCommand(SocketRequest req, PrintWriter out) {
        try {
            guardAuth(req.getCommand());
            Map<String, Object> p = req.getPayload();

            switch (req.getCommand()) {

                case "REGISTER" -> {
                    RegisterRequest r = cast(p, RegisterRequest.class);
                    send(out, "OK", "Registered", userService.register(r));

                }

                case "LOGIN" -> {
                    LoginRequest r = cast(p, LoginRequest.class);
                    User u = userService.login(r);
                    authenticatedUserId = u.getId();
                    send(out, "OK", "Logged in", u);

                }

                case "DEPOSIT" -> {
                    double amount = ((Number) p.get("amount")).doubleValue();
                    send(out, "OK", "Deposited", userService.deposit(authenticatedUserId, amount));
                }

                case "ADD_ITEM" -> {
                    AddItemRequest r = cast(p, AddItemRequest.class);
                    send(out, "OK", "Item added", itemService.addItem(authenticatedUserId, r));
                }

                case "EDIT_ITEM" -> {
                    String id = (String) p.get("itemId");
                    UpdateItemRequest r = cast(p, UpdateItemRequest.class);
                    send(out, "OK", "Updated", itemService.editItem(id, authenticatedUserId, r));
                }

                case "REMOVE_ITEM" -> {
                    itemService.removeItem((String) p.get("itemId"), authenticatedUserId);
                    send(out, "OK", "Removed", null);
                }

                case "SEARCH" -> send(out, "OK", null,
                        itemService.searchItems((String) p.get("query")));

                case "PURCHASE" -> {
                    PurchaseRequest r = cast(p, PurchaseRequest.class);
                    send(out, "OK", "Purchased",
                            transactionService.purchaseItem(
                                    authenticatedUserId, r.getItemId(), r.getQuantity()));
                }

                case "ACCOUNT" -> send(out, "OK", null,
                        userService.getAccountInfo(authenticatedUserId));
            }
        } catch (Exception e) {
            send(out, "ERROR", e.getMessage(), null);
        }
    }

    private void guardAuth(String command) {
        boolean isPublic = "REGISTER".equals(command)
                || "LOGIN".equals(command)
                || "DISCONNECT".equals(command);

        if (!isPublic && authenticatedUserId == null)
            throw new SecurityException("Not authenticated. Please LOGIN first.");
    }

    private <T> T cast(Map<String, Object> payload, Class<T> type) {
        return mapper.convertValue(payload, type);
    }


    private void send(PrintWriter out, String status, String message, Object data) {
        try {
            out.println(mapper.writeValueAsString(
                    new SocketResponse(status, message, data)));
        } catch (Exception e) {
            log.error("Failed to serialize response", e);
        }
    }
}