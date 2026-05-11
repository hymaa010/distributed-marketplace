package org.team13.marketplace.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileRequest {
    @NotBlank(message = "User ID is required")
    private String userId;
}
