package com.creditas.loan.simulator.domain;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Installment {
  private String number;
  private LocalDate date;
  private Double value;
}
