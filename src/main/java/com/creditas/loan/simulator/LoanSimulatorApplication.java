package com.creditas.loan.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public final class LoanSimulatorApplication {

  private LoanSimulatorApplication() {
    // Prevent instantiation
  }

  public static void main(final String[] args) {
    SpringApplication.run(LoanSimulatorApplication.class, args);
  }

}
