package org.team13.marketplace.socket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketRequest {
    private String command;
    private Map<String, Object> payload;
}
