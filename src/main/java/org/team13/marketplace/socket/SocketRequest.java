package org.team13.marketplace.socket;

import lombok.Data;

import java.util.Map;

@Data
public class SocketRequest {
    private String command;
    private Map<String, Object> payload;
}
