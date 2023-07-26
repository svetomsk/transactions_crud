package com.svetomsk.crudtransactions.dao;


import com.svetomsk.crudtransactions.entity.TransferCodeEntity;
import com.svetomsk.crudtransactions.entity.TransferEntity;
import com.svetomsk.crudtransactions.entity.UserEntity;
import com.svetomsk.crudtransactions.repository.TransferCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransferCodeDao {
    private final TransferCodeRepository repository;

    public TransferCodeEntity createAndSaveCode(UserEntity user, TransferEntity transfer) {
        return repository.save(TransferCodeEntity.builder()
                .code(generateCode())
                .sender(user)
                .transfer(transfer)
                .build());
    }

    public TransferCodeEntity findByCode(String code) {
        return repository.findByCode(code).orElseThrow(
                () -> new NoSuchElementException("Code " + code + " is not found in repository")
        );
    }

    private String generateCode() {
        Random rnd = new Random();
        List<Character> result = new ArrayList<>();
        int length = 6;
        for (int i = 0; i < length / 2; i++) {
            result.add((char) ('0' + rnd.nextInt(0, 9)));
        }
        for (int i = 0; i < length / 2; i++) {
            boolean isUpper = rnd.nextInt() % 2 == 0;
            int charCode = 'a' + rnd.nextInt(0, 26);
            if (isUpper)
                result.add((char) Character.toUpperCase(charCode));
            else
                result.add((char) charCode);
        }
        Collections.shuffle(result);
        return result.stream().map(String::valueOf).collect(Collectors.joining(""));
    }
}
