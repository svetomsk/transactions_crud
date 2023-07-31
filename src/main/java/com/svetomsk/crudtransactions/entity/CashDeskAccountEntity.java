package com.svetomsk.crudtransactions.entity;

import com.svetomsk.crudtransactions.enums.TransferCurrency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cash_desk_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashDeskAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cash_desk_id", nullable = false)
    private CashDeskEntity cashDesk;

    @Enumerated(EnumType.STRING)
    private TransferCurrency currency;

    private Double balance;

    @Builder.Default
    private boolean enabled = true;

    @Version
    private long version;
}
