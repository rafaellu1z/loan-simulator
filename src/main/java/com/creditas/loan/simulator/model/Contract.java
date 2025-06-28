package com.creditas.loan.simulator.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Contract {

    private String number;
    private Double totalValue;

    @Setter(AccessLevel.NONE)
    List<Installment> installments;
}
