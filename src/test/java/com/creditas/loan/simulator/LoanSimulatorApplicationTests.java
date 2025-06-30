package com.creditas.loan.simulator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = LoanSimulatorApplication.class)
public final class LoanSimulatorApplicationTests {

  @Test
  public void applicationContextLoads() {
  }

  @Test
  public void mainMethodStartsApplication() {
    String[] args = {"--server.port=0", "--spring.profiles.active=test"};
    LoanSimulatorApplication.main(args);
  }
}
