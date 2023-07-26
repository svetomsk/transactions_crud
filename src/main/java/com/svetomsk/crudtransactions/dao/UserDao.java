package com.svetomsk.crudtransactions.dao;

import com.svetomsk.crudtransactions.dto.UserDto;
import com.svetomsk.crudtransactions.entity.UserEntity;
import com.svetomsk.crudtransactions.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDao {
    private final UserRepository repository;

    public UserEntity findByInfoOrCreate(UserDto userDto) {
        Optional<UserEntity> maybeUser = repository.findByPhone(userDto.getPhoneNumber());
        // TODO: handle concurrent user creation
        return maybeUser.orElseGet(() -> createNewUsers(userDto));
    }

    private UserEntity createNewUsers(UserDto userDto) {
        UserEntity entity = UserEntity.builder()
                .phone(userDto.getPhoneNumber())
                .name(userDto.getName())
                .build();
        return repository.save(entity);
    }
}
