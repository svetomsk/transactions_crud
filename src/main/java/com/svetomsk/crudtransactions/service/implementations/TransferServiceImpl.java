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
import com.svetomsk.crudtransactions.service.interfaces.TransferService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

        CashDeskEntity cashDesk = cashDeskDao.findEntityById(request.getCashDeskId());

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
                .build());

        // create code for transfer
        TransferCodeEntity codeEntity = transferCodeDao.createAndSaveCode(sender, transferEntity);

        // update cash desk balance
        cashDeskDao.deposit(cashDesk, request.getAmount());
        return new TransferCodeDto(codeEntity.getCode());
    }

    @Override
    public TransferDto issueTransfer(IssueTransferRequest request) {
        String code = request.getSecretCode();
        UserDto issuer = request.getIssuer();
        CashDeskEntity cashDesk = cashDeskDao.findEntityById(request.getCashDeskId());

        // find code
        TransferCodeEntity codeEntity = transferCodeDao.findByCode(code);

        // check data equality
        TransferEntity transfer = codeEntity.getTransfer();
        UserEntity receiver = transfer.getReceiver();
        if (!receiver.getName().equals(issuer.getName()) ||
                !receiver.getPhone().equals(issuer.getPhoneNumber())) {
            throw new IllegalArgumentException("User data does not match");
        } else {
            log.info("User data match");
        }

        // update cash desk balance
        cashDeskDao.withdraw(cashDesk, transfer.getAmount());

        // update transfer status
        transfer.setStatus(TransferStatus.FINISHED);
        return TransferDto.entityToDto(transferDao.saveTransfer(transfer));
    }
}
