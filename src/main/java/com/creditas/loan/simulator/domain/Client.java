package com.creditas.loan.simulator.domain;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    private Date birthDate;

    @Setter(AccessLevel.NONE)
    List<Contract> contracts;
}
