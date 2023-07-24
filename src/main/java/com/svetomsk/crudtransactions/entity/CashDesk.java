package com.svetomsk.crudtransactions.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cash_desk")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashDesk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double balance;
}
