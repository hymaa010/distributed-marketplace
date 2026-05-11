package org.team13.marketplace.mapper;

import org.team13.marketplace.dto.user.UserDto;
import org.team13.marketplace.model.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
