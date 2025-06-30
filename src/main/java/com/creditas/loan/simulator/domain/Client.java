package com.creditas.loan.simulator.domain;

import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "email")
@NoArgsConstructor
public class Client {

  private String id;
  private String name;
  private String email;
  private LocalDate birthDate;

  @Setter(AccessLevel.NONE)
  private List<Contract> contracts = List.of();

  public Client(final String id,
                final String name,
                final String email,
                final LocalDate birthDate) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.birthDate = birthDate;
  }

}
