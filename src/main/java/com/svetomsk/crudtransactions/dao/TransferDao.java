package com.svetomsk.crudtransactions.dao;

import com.svetomsk.crudtransactions.components.DtoMapper;
import com.svetomsk.crudtransactions.dto.TransferDto;
import com.svetomsk.crudtransactions.entity.TransferEntity;
import com.svetomsk.crudtransactions.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransferDao {
    private final TransferRepository transferRepository;
    private final DtoMapper dtoMapper;

    public TransferEntity saveTransfer(TransferEntity entity) {
        return transferRepository.save(entity);
    }

    public TransferDto saveTransferDto(TransferEntity entity) {
        return dtoMapper.toDto(transferRepository.save(entity));
    }

    public List<TransferDto> findAll(Specification<TransferEntity> specification, Pageable pageable) {
        return transferRepository.findAll(specification, pageable)
                .stream()
                .map(dtoMapper::toDto)
                .toList();
    }
}
