package com.svetomsk.crudtransactions.dao;

import com.svetomsk.crudtransactions.dto.UserDto;
import com.svetomsk.crudtransactions.entity.UserEntity;
import com.svetomsk.crudtransactions.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserDao dao;

    @Test
    public void findByInfoOrCreate_userFound_userReturned() {
        var userEntity = UserEntity.builder().id(123L).build();
        var phone = "phone number";
        when(repository.findByPhone(any())).thenReturn(Optional.ofNullable(userEntity));
        var actual = dao.findByInfoOrCreate(UserDto.builder().phone(phone).build());
        assertEquals(userEntity, actual);
        verify(repository, times(1)).findByPhone(phone);
    }

    @Test
    public void findByInfoOrCreate_userNotFound_userCreated() {
        when(repository.findByPhone(any())).thenReturn(Optional.empty());
        String name = "name";
        String phone = "phone";
        var dto = UserDto.builder()
                .name(name)
                .phone(phone)
                .build();
        var userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        when(repository.save(userCaptor.capture())).thenAnswer(value -> value.getArguments()[0]);
        dao.findByInfoOrCreate(dto);
        var value = userCaptor.getValue();
        assertEquals(name, value.getName());
        assertEquals(phone, value.getPhone());
        verify(repository, times(1)).findByPhone(any());
        verify(repository, times(1)).save(any());
    }

    @Test
    public void count_repositoryCalled() {
        long count = 10L;
        when(repository.count()).thenReturn(count);
        var actual = dao.count();
        assertEquals(count, actual);
        verify(repository, times(1)).count();
    }

    @Test
    public void findByInfoOrCreate_duplicateUser_existingUserReturned() {
        var userEntity = UserEntity.builder().id(123L).build();
        final int[] invocationCount = {0};
        when(repository.findByPhone(any())).thenAnswer(value -> {
            if (invocationCount[0] == 0) {
                invocationCount[0]++;
                return Optional.empty();
            }
            return Optional.of(userEntity);
        });
        when(repository.save(any())).thenThrow(new DataIntegrityViolationException("Unique constraint violation"));
        var actual = dao.findByInfoOrCreate(new UserDto());
        assertEquals(userEntity, actual);
    }

}