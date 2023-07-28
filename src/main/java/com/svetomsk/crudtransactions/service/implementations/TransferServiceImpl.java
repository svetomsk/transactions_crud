package com.svetomsk.crudtransactions.service.implementations;

import com.svetomsk.crudtransactions.dao.*;
import com.svetomsk.crudtransactions.dto.TransferCodeDto;
import com.svetomsk.crudtransactions.dto.TransferDto;
import com.svetomsk.crudtransactions.dto.UserDto;
import com.svetomsk.crudtransactions.entity.*;
import com.svetomsk.crudtransactions.enums.TransferStatus;
import com.svetomsk.crudtransactions.model.CreateTransferRequest;
import com.svetomsk.crudtransactions.model.IssueTransferRequest;
import com.svetomsk.crudtransactions.model.ListTransfersRequest;
import com.svetomsk.crudtransactions.model.TransfersListResponse;
import com.svetomsk.crudtransactions.repository.specifications.PredicateWithCondition;
import com.svetomsk.crudtransactions.service.interfaces.CurrencyExchangeService;
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

import static com.svetomsk.crudtransactions.repository.specifications.TransferSpecifications.createComplexPredicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final UserDao userDao;
    private final CashDeskDao cashDeskDao;
    private final TransferDao transferDao;
    private final CashDeskAccountDao accountDao;
    private final TransferCodeDao transferCodeDao;
    private final CurrencyExchangeService exchangeService;
    private final List<PredicateWithCondition<ListTransfersRequest, TransferEntity>> predicates;

    @Override
    @Transactional
    public TransferCodeDto createTransfer(CreateTransferRequest request) {
        UserDto senderDto = request.getSenderInfo();
        UserDto receiverDto = request.getReceiverInfo();

        // update account balance
        CashDeskEntity cashDesk = cashDeskDao.findEntityById(request.getCashDeskId());
        CashDeskAccountEntity account = accountDao.findAndDeposit(cashDesk, request.getCurrency(), request.getAmount());

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
        double amount = exchangeService.convert(transfer.getCurrency(), transfer.getAmount(), request.getCurrency());
        CashDeskEntity cashDesk = transfer.getCashDesk();

        accountDao.findAndWithdraw(cashDesk, request.getCurrency(), amount);

        // update transfer status
        transfer.setStatus(TransferStatus.FINISHED);
        return transferDao.saveTransferDto(transfer);
    }

    private boolean isIssuerEqualToReceiver(UserDto issuer, UserEntity receiver) {
        return issuer.getName().equals(receiver.getName()) &&
                issuer.getPhone().equals(receiver.getPhone());
    }

    @Override
    public TransfersListResponse listTransfers(ListTransfersRequest request) {
        // create sorting and pageable objects
        Sort sort = Sort.by(request.getOrder(), request.getSortBy().getColumn());
        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);

        // include necessary specifications for filtration
        List<Specification<TransferEntity>> specifications = new ArrayList<>();
        for (var predicate : predicates) {
            var specification = predicate.makeSpecification(request);
            if (specification != null) {
                specifications.add(specification);
            }
        }

        Specification<TransferEntity> unitedPredicates = createComplexPredicate(specifications);
        List<TransferDto> transfers = transferDao.findAll(unitedPredicates, pageable);
        return new TransfersListResponse(transfers, request.getPageNumber(), transfers.size());
    }
}
