package com.creditas.loan.simulator.domain;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "email")
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    private String id;
    private String name;
    private String email;
    private Date birthDate;

    @Setter(AccessLevel.NONE)
    List<Contract> contracts;
}
