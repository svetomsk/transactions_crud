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
import com.svetomsk.crudtransactions.enums.TransferCurrency;
import com.svetomsk.crudtransactions.enums.TransferOrderParam;
import com.svetomsk.crudtransactions.enums.TransferStatus;
import com.svetomsk.crudtransactions.model.CreateTransferRequest;
import com.svetomsk.crudtransactions.model.IssueTransferRequest;
import com.svetomsk.crudtransactions.model.ListTransfersRequest;
import com.svetomsk.crudtransactions.repository.TransferSpecifications;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

import static com.svetomsk.crudtransactions.repository.TransferSpecifications.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceImplTest {
    @Mock
    private TransferDao transferDao;
    @Mock
    private TransferCodeDao codeDao;
    @Mock
    private CashDeskDao cashDeskDao;
    @Mock
    private UserDao userDao;

    @InjectMocks
    private TransferServiceImpl transferService;
    private UserDto sender = new UserDto("Sender", "Sender phone");
    private UserEntity senderEntity = new UserEntity(1L, "Sender", "Sender phone");
    private UserDto receiver = new UserDto("Receiver", "Receiver phone");
    private UserEntity receiverEntity = new UserEntity(2L, "Receiver", "Receiver phone");


    @Test
    public void createTransfer_correctRequest_transferCreatedAndDeskWithdrewAndCodeGenerated() {
        double amount = 500.0;
        TransferCurrency currency = TransferCurrency.KGS;
        String comment = "Transfer comment";
        long cashDeskId = 1L;
        String code = "Some code";
        CashDeskEntity cashDesk = CashDeskEntity.builder()
                .balance(2000.0)
                .id(cashDeskId)
                .build();
        CreateTransferRequest request = CreateTransferRequest.builder()
                .amount(amount)
                .currency(currency)
                .cashDeskId(cashDeskId)
                .senderInfo(sender)
                .receiverInfo(receiver)
                .comment(comment)
                .build();
        when(cashDeskDao.findEntityById(cashDeskId)).thenReturn(cashDesk);
        when(userDao.findByInfoOrCreate(sender)).thenReturn(senderEntity);
        when(userDao.findByInfoOrCreate(receiver)).thenReturn(receiverEntity);
        when(transferDao.saveTransfer(any())).thenAnswer(answer -> answer.getArguments()[0]);
        var codeEntity = new TransferCodeEntity(1L, code, senderEntity, new TransferEntity());
        when(codeDao.createAndSaveCode(any(), any())).thenReturn(codeEntity);

        TransferCodeDto actual = transferService.createTransfer(request);
        assertEquals(code, actual.getTransferCode());

        verify(cashDeskDao, times(1)).findEntityById(cashDeskId);
        verify(userDao, times(1)).findByInfoOrCreate(sender);
        verify(userDao, times(1)).findByInfoOrCreate(receiver);
        var captor = ArgumentCaptor.forClass(TransferEntity.class);
        verify(transferDao, times(1)).saveTransfer(captor.capture());

        assertNotNull(captor.getValue());
        TransferEntity value = captor.getValue();
        assertEquals(senderEntity, value.getSender());
        assertEquals(receiverEntity, value.getReceiver());
        assertEquals(TransferCurrency.KGS, value.getCurrency());
        assertEquals(cashDesk, value.getCashDesk());
        assertEquals(amount, value.getAmount());
        assertEquals(comment, value.getComment());
        assertEquals(TransferStatus.CREATED, value.getStatus());

        verify(codeDao, times(1)).createAndSaveCode(senderEntity, captor.getValue());
        verify(cashDeskDao, times(1)).deposit(cashDesk, amount);
    }

    @Test
    public void issueTransfer_requestOk_transferIssued() {
        var cashDeskId = 1L;
        var cashDesk = CashDeskEntity.builder()
                .balance(200.0)
                .id(cashDeskId)
                .build();
        var amount = 100.0;
        when(cashDeskDao.findEntityById(cashDeskId)).thenReturn(cashDesk);
        var transferId = 123L;
        var transferEntity = TransferEntity.builder()
                .id(transferId)
                .amount(amount)
                .status(TransferStatus.CREATED)
                .receiver(receiverEntity)
                .cashDesk(cashDesk)
                .sender(senderEntity)
                .build();
        var code = "some code";
        var codeEntity = new TransferCodeEntity(1L, code, null, transferEntity);
        when(codeDao.findByCode(any())).thenReturn(codeEntity);
        when(transferDao.saveTransfer(any())).thenAnswer(answer -> answer.getArguments()[0]);

        var request = new IssueTransferRequest(receiver, code, cashDeskId);
        var actual = transferService.issueTransfer(request);
        assertEquals(transferId, actual.getId());
        assertEquals(TransferStatus.FINISHED, actual.getStatus());

        verify(cashDeskDao, times(1)).findEntityById(cashDeskId);
        verify(codeDao, times(1)).findByCode(code);
        verify(cashDeskDao, times(1)).withdraw(cashDesk, amount);
        verify(transferDao, times(1)).saveTransfer(transferEntity);
    }

    @Test
    public void issueTransfer_cashDeskBalanceIsLessThanTransfer_exceptionThrown() {
        var cashDeskBalance = 100.0;
        var transferAmount = 120.0;
        var cashDeskEntity = CashDeskEntity.builder().balance(cashDeskBalance).build();
        var transferEntity = TransferEntity.builder().amount(transferAmount).cashDesk(cashDeskEntity).build();
        var codeEntity = TransferCodeEntity.builder().transfer(transferEntity).build();
        when(cashDeskDao.findEntityById(any())).thenReturn(cashDeskEntity);
        when(codeDao.findByCode(any())).thenReturn(codeEntity);
        assertThrows(IllegalArgumentException.class, () -> {
            transferService.issueTransfer(new IssueTransferRequest(new UserDto(), "code", 1L));
        });
    }

    @Test
    public void issueTransfer_issuerAndReceiverDiffers_exceptionThrown() {
        var cashDeskBalance = 150.0;
        var transferAmount = 120.0;
        var anotherReceiver = new UserEntity(1L, "Another name", "Another phone");
        var cashDeskEntity = CashDeskEntity.builder().balance(cashDeskBalance).build();
        var transferEntity = TransferEntity.builder().amount(transferAmount).cashDesk(cashDeskEntity).receiver(anotherReceiver).build();
        var codeEntity = TransferCodeEntity.builder().transfer(transferEntity).build();
        when(cashDeskDao.findEntityById(any())).thenReturn(cashDeskEntity);
        when(codeDao.findByCode(any())).thenReturn(codeEntity);
        assertThrows(IllegalArgumentException.class, () -> {
            transferService.issueTransfer(new IssueTransferRequest(receiver, "code", 1L));
        });
    }

    @Captor
    private ArgumentCaptor<ArrayList<Specification<TransferEntity>>> specificationCaptor;

    @Test
    public void listTransfers_correctRequest_necessaryFiltersApplied() {
        var pageNumber = 0;
        var pageSize = 10;
        var request = ListTransfersRequest.builder()
                .order(Sort.Direction.DESC)
                .pageNumber(0)
                .pageSize(10)
                .sortBy(TransferOrderParam.SENDER)
                .receiver(receiver)
                .sender(sender)
                .status(TransferStatus.FINISHED)
                .build();
        try (MockedStatic<TransferSpecifications> staticMock = mockStatic(TransferSpecifications.class);
             MockedStatic<TransferDto> dtoMock = mockStatic(TransferDto.class)) {
            staticMock.when(() -> senderPredicate(any())).thenCallRealMethod();
            staticMock.when(() -> receiverPredicate(any())).thenCallRealMethod();
            staticMock.when(() -> statusPredicate(any())).thenCallRealMethod();
            staticMock.when(() -> createComplexPredicate(anyList())).thenCallRealMethod();
            dtoMock.when(TransferDto::builder).thenCallRealMethod();
            dtoMock.when(() -> TransferDto.entityToDto(any()))
                    .thenAnswer(value -> TransferDto.builder().id(((TransferEntity) value.getArgument(0)).getId()).build());
            var transferEntities = new ArrayList<TransferEntity>();
            transferEntities.add(TransferEntity.builder().id(1L).build());
            transferEntities.add(TransferEntity.builder().id(2L).build());
            when(transferDao.findAll(any(), any())).thenReturn(transferEntities);

            var actualList = transferService.listTransfers(request);
            assertEquals(transferEntities.size(), actualList.size());

            staticMock.verify(() -> senderPredicate(sender.getPhoneNumber()), times(1));
            staticMock.verify(() -> receiverPredicate(receiver.getPhoneNumber()), times(1));
            staticMock.verify(() -> statusPredicate(TransferStatus.FINISHED), times(1));
            staticMock.verify(() -> createComplexPredicate(specificationCaptor.capture()), times(1));
            assertEquals(3, specificationCaptor.getValue().size());
            var pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
            verify(transferDao, times(1)).findAll(any(), pageableCaptor.capture());
            assertEquals(pageNumber, pageableCaptor.getValue().getPageNumber());
            assertEquals(pageSize, pageableCaptor.getValue().getPageSize());
            assertEquals(Sort.by(request.getOrder(), request.getSortBy().getColumn()), pageableCaptor.getValue().getSort());
        }
    }
}