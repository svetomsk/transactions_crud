package com.svetomsk.crudtransactions.service.implementations;

import com.svetomsk.crudtransactions.dao.CashDeskDao;
import com.svetomsk.crudtransactions.dao.TransferCodeDao;
import com.svetomsk.crudtransactions.dao.TransferDao;
import com.svetomsk.crudtransactions.dao.UserDao;
import com.svetomsk.crudtransactions.dto.TransferCodeDto;
import com.svetomsk.crudtransactions.dto.UserDto;
import com.svetomsk.crudtransactions.entity.CashDeskEntity;
import com.svetomsk.crudtransactions.entity.TransferCodeEntity;
import com.svetomsk.crudtransactions.entity.TransferEntity;
import com.svetomsk.crudtransactions.entity.UserEntity;
import com.svetomsk.crudtransactions.enums.TransferCurrency;
import com.svetomsk.crudtransactions.model.CreateTransferRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    public void createTransfer_correctRequest_transferCreatedAndDeskWithdrewAndCodeGenerated() {
        UserDto sender = new UserDto("Sender", "Sender number");
        UserEntity senderEntity = new UserEntity(1L, "Sender", "Sender phone");
        UserDto receiver = new UserDto("Receiver", "Receiver number");
        UserEntity receiverEntity = new UserEntity(2L, "Receiver", "Receiver phone");
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
        verify(codeDao, times(1)).createAndSaveCode(senderEntity, captor.getValue());
        verify(cashDeskDao, times(1)).deposit(cashDesk, amount);
    }

}