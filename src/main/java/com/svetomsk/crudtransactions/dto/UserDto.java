package com.svetomsk.crudtransactions.dto;

import com.svetomsk.crudtransactions.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String name;
    private String phoneNumber;

    public static UserEntity dtoToEntity(UserDto userDto) {
        return UserEntity.builder()
                .phone(userDto.getPhoneNumber())
                .name(userDto.getName())
                .build();
    }

    public static UserDto entityToDto(UserEntity entity) {
        return UserDto.builder()
                .phoneNumber(entity.getPhone())
                .name(entity.getName())
                .build();
    }
}
