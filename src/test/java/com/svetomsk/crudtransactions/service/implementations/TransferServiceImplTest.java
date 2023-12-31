package com.svetomsk.crudtransactions.service.implementations;

import com.svetomsk.crudtransactions.dao.*;
import com.svetomsk.crudtransactions.dto.TransferCodeDto;
import com.svetomsk.crudtransactions.dto.TransferDto;
import com.svetomsk.crudtransactions.dto.UserDto;
import com.svetomsk.crudtransactions.entity.*;
import com.svetomsk.crudtransactions.enums.TransferCurrency;
import com.svetomsk.crudtransactions.enums.TransferOrderParam;
import com.svetomsk.crudtransactions.enums.TransferStatus;
import com.svetomsk.crudtransactions.model.CreateTransferRequest;
import com.svetomsk.crudtransactions.model.IssueTransferRequest;
import com.svetomsk.crudtransactions.model.ListTransfersRequest;
import com.svetomsk.crudtransactions.repository.specifications.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static com.svetomsk.crudtransactions.repository.specifications.TransferSpecifications.createComplexPredicate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceImplTest {
    @Mock
    private TransferDao transferDao;
    @Mock
    private FixedCurrencyExchangeServiceImpl exchangeService;
    @Mock
    private TransferCodeDao codeDao;
    @Mock
    private CashDeskDao cashDeskDao;
    @Mock
    private UserDao userDao;
    @Mock
    private CashDeskAccountDao accountDao;
    @Mock
    private SenderPhonePredicate senderPhonePredicate;
    @Mock
    private ReceiverPhonePredicate receiverPhonePredicate;
    @Mock
    private StatusPredicate statusPredicate;
    @Spy
    List<PredicateWithCondition<ListTransfersRequest, TransferEntity>> predicates = new ArrayList<>();

    @InjectMocks
    private TransferServiceImpl transferService;
    private UserDto sender = new UserDto(1L, "Sender", "Sender phone");
    private UserEntity senderEntity = UserEntity.builder()
            .id(1L)
            .name("Sender")
            .phone("Sender phone")
            .build();
    private UserDto receiver = new UserDto(2L, "Receiver", "Receiver phone");
    private UserEntity receiverEntity = UserEntity.builder()
            .id(2L)
            .name("Receiver")
            .phone("Receiver phone")
            .build();


    @Test
    public void createTransfer_correctRequest_transferCreatedAndDeskWithdrewAndCodeGenerated() {
        double amount = 500.0;
        TransferCurrency currency = TransferCurrency.KGS;
        String comment = "Transfer comment";
        long cashDeskId = 1L;
        String code = "Some code";
        CashDeskEntity cashDesk = CashDeskEntity.builder()
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
        when(accountDao.findAndDeposit(cashDesk, TransferCurrency.KGS, amount)).thenReturn(new CashDeskAccountEntity());
        when(userDao.findByInfoOrCreate(sender)).thenReturn(senderEntity);
        when(userDao.findByInfoOrCreate(receiver)).thenReturn(receiverEntity);
        when(cashDeskDao.findEntityById(any())).thenReturn(cashDesk);
        when(transferDao.saveTransfer(any())).thenAnswer(answer -> answer.getArguments()[0]);
        var codeEntity = TransferCodeEntity.builder().id(1L).code(code).sender(senderEntity).transfer(new TransferEntity()).build();
        when(codeDao.createAndSaveCode(any(), any())).thenReturn(codeEntity);

        TransferCodeDto actual = transferService.createTransfer(request);
        assertEquals(code, actual.getTransferCode());
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
    }

    @Test
    public void issueTransfer_requestOk_transferIssued() {
        var cashDeskId = 1L;
        var cashDesk = CashDeskEntity.builder()
                .id(cashDeskId)
                .build();
        var amount = 100.0;
        var transferId = 123L;
        var transferEntity = TransferEntity.builder()
                .id(transferId)
                .amount(amount)
                .status(TransferStatus.CREATED)
                .receiver(receiverEntity)
                .cashDesk(cashDesk)
                .sender(senderEntity)
                .currency(TransferCurrency.KGS)
                .build();
        var code = "some code";
        var codeEntity = TransferCodeEntity.builder()
                .id(1L)
                .code(code)
                .transfer(transferEntity)
                .build();
        when(codeDao.findAndMarkIssued(any())).thenReturn(codeEntity);
        var dtoResult = TransferDto.builder().id(1L).currency(TransferCurrency.KGS).build();
        when(transferDao.saveTransferDto(any())).thenReturn(dtoResult);
        when(exchangeService.convert(TransferCurrency.KGS, amount, TransferCurrency.KGS)).thenReturn(amount);

        var request = new IssueTransferRequest(receiver, code, cashDeskId, TransferCurrency.KGS);
        var actual = transferService.issueTransfer(request);
        assertEquals(dtoResult, actual);

        verify(codeDao, times(1)).findAndMarkIssued(code);
        verify(accountDao, times(1)).findAndWithdraw(cashDesk, TransferCurrency.KGS, amount);

        var transferCaptor = ArgumentCaptor.forClass(TransferEntity.class);
        verify(transferDao, times(1)).saveTransferDto(transferCaptor.capture());
        assertEquals(TransferStatus.FINISHED, transferCaptor.getValue().getStatus());
    }

    @Test
    public void issueTransfer_issuerAndReceiverDiffers_exceptionThrown() {
        var cashDeskBalance = 150.0;
        var transferAmount = 120.0;
        var anotherReceiver = UserEntity.builder()
                .id(1L)
                .name("Another name")
                .phone("Another phone")
                .build();
        var cashDeskEntity = CashDeskEntity.builder().build();
        var transferEntity = TransferEntity.builder().amount(transferAmount).cashDesk(cashDeskEntity).receiver(anotherReceiver).build();
        var codeEntity = TransferCodeEntity.builder().transfer(transferEntity).build();
        when(codeDao.findAndMarkIssued(any())).thenReturn(codeEntity);
        assertThrows(IllegalArgumentException.class, () -> {
            transferService.issueTransfer(new IssueTransferRequest(receiver, "code", 1L, TransferCurrency.KGS));
        });
    }

    @Captor
    private ArgumentCaptor<ArrayList<Specification<TransferEntity>>> specificationCaptor;

    @Test
    public void listTransfers_correctRequest_necessaryFiltersApplied() {
        predicates.add(receiverPhonePredicate);
        predicates.add(senderPhonePredicate);
        predicates.add(statusPredicate);
        Specification<TransferEntity> specification = (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("status"), TransferStatus.CREATED);
        for (var predicate : predicates) {
            when(predicate.makeSpecification(any())).thenReturn(specification);
        }

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
        try (MockedStatic<TransferSpecifications> staticMock = mockStatic(TransferSpecifications.class)) {
            staticMock.when(() -> createComplexPredicate(anyList())).thenCallRealMethod();
            var transferEntities = new ArrayList<TransferDto>();
            transferEntities.add(TransferDto.builder().id(1L).build());
            transferEntities.add(TransferDto.builder().id(2L).build());
            when(transferDao.findAll(any(), any())).thenReturn(transferEntities);

            var actualList = transferService.listTransfers(request);
            assertEquals(transferEntities.size(), actualList.getTransfers().size());
            for (var predicate : predicates) {
                verify(predicate, times(1)).makeSpecification(any());
            }
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