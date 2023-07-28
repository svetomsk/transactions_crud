package com.svetomsk.crudtransactions.dao;

import com.svetomsk.crudtransactions.dto.UserDto;
import com.svetomsk.crudtransactions.entity.UserEntity;
import com.svetomsk.crudtransactions.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDao {
    private final UserRepository repository;

    public UserEntity findByInfoOrCreate(UserDto userDto) {
        Optional<UserEntity> maybeUser = repository.findByPhone(userDto.getPhone());
        return maybeUser.orElseGet(() -> createNewUsers(userDto));
    }

    private UserEntity createNewUsers(UserDto userDto) {
        UserEntity entity = UserEntity.builder()
                .phone(userDto.getPhone())
                .name(userDto.getName())
                .build();
        try {
            return repository.save(entity);
        } catch (DataIntegrityViolationException exc) {
            log.info("User already exists");
            return repository.findByPhone(userDto.getPhone()).get();
        }
    }

    public long count() {
        return repository.count();
    }
}
