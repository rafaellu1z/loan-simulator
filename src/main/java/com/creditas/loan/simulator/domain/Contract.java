package com.creditas.loan.simulator.domain;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Contract {

  private String number;
  private Double totalValue;

  @Setter(AccessLevel.NONE)
  private List<Installment> installments = List.of();

  public Contract(final String number,
                  final Double totalValue) {
    this.number = number;
    this.totalValue = totalValue;
  }
}
