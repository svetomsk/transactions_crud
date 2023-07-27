package com.svetomsk.crudtransactions.service.implementations;

import com.svetomsk.crudtransactions.dao.CashDeskDao;
import com.svetomsk.crudtransactions.dao.TransferCodeDao;
import com.svetomsk.crudtransactions.dao.TransferDao;
import com.svetomsk.crudtransactions.dao.UserDao;
import com.svetomsk.crudtransactions.dto.TransferCodeDto;
import com.svetomsk.crudtransactions.dto.TransferDto;
import com.svetomsk.crudtransactions.dto.UserDto;
import com.svetomsk.crudtransactions.entity.CashDeskEntity;
import com.svetomsk.crudtransactions.entity.TransferCodeEntity;
import com.svetomsk.crudtransactions.entity.TransferEntity;
import com.svetomsk.crudtransactions.entity.UserEntity;
import com.svetomsk.crudtransactions.enums.TransferStatus;
import com.svetomsk.crudtransactions.model.CreateTransferRequest;
import com.svetomsk.crudtransactions.model.IssueTransferRequest;
import com.svetomsk.crudtransactions.model.ListTransfersRequest;
import com.svetomsk.crudtransactions.model.TransfersListResponse;
import com.svetomsk.crudtransactions.service.interfaces.TransferService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.svetomsk.crudtransactions.repository.TransferSpecifications.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final UserDao userDao;
    private final CashDeskDao cashDeskDao;
    private final TransferDao transferDao;
    private final TransferCodeDao transferCodeDao;

    @Override
    @Transactional
    public TransferCodeDto createTransfer(CreateTransferRequest request) {
        UserDto senderDto = request.getSenderInfo();
        UserDto receiverDto = request.getReceiverInfo();

        // update cash desk balance
        CashDeskEntity cashDesk = cashDeskDao.findAndDeposit(request.getCashDeskId(), request.getAmount());

        // retrieve or create users
        UserEntity sender = userDao.findByInfoOrCreate(senderDto);
        UserEntity receiver = userDao.findByInfoOrCreate(receiverDto);

        // create transfer entity
        TransferEntity transferEntity = transferDao.saveTransfer(TransferEntity.builder()
                .sender(sender)
                .receiver(receiver)
                .currency(request.getCurrency())
                .cashDesk(cashDesk)
                .amount(request.getAmount())
                .comment(request.getComment())
                .status(TransferStatus.CREATED)
                .enabled(true)
                .build());

        // create code for transfer
        TransferCodeEntity codeEntity = transferCodeDao.createAndSaveCode(sender, transferEntity);

        return new TransferCodeDto(codeEntity.getCode());
    }

    @Override
    @Transactional
    public TransferDto issueTransfer(IssueTransferRequest request) {
        String code = request.getSecretCode();
        UserDto issuer = request.getIssuer();

        // find code or fail if code already used
        TransferCodeEntity codeEntity = transferCodeDao.findAndMarkIssued(code);

        // check data equality
        TransferEntity transfer = codeEntity.getTransfer();
        UserEntity receiver = transfer.getReceiver();
        if (!isIssuerEqualToReceiver(issuer, receiver)) {
            throw new IllegalArgumentException("User data does not match");
        } else {
            log.info("User data match");
        }

        // update cash desk balance
        cashDeskDao.findAndWithdraw(request.getCashDeskId(), transfer.getAmount());

        // update transfer status
        transfer.setStatus(TransferStatus.FINISHED);
        return transferDao.saveTransferDto(transfer);
    }

    private boolean isIssuerEqualToReceiver(UserDto issuer, UserEntity receiver) {
        return issuer.getName().equals(receiver.getName()) &&
                issuer.getPhoneNumber().equals(receiver.getPhone());
    }

    @Override
    public TransfersListResponse listTransfers(ListTransfersRequest request) {
        // create sorting and pageable objects
        Sort sort = Sort.by(request.getOrder(), request.getSortBy().getColumn());
        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);

        // include necessary specifications for filtration
        List<Specification<TransferEntity>> predicates = new ArrayList<>();
        if (request.getSender() != null) {
            predicates.add(senderPredicate(request.getSender().getPhoneNumber()));
        }
        if (request.getReceiver() != null) {
            predicates.add(receiverPredicate(request.getReceiver().getPhoneNumber()));
        }
        if (request.getStatus() != null) {
            predicates.add(statusPredicate(request.getStatus()));
        }
        Specification<TransferEntity> unitedPredicates = createComplexPredicate(predicates);
        List<TransferDto> transfers = transferDao.findAll(unitedPredicates, pageable);
        return new TransfersListResponse(transfers, request.getPageNumber(), transfers.size());
    }
}
