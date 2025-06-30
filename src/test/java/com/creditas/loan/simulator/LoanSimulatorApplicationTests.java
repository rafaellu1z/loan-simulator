package com.creditas.loan.simulator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = LoanSimulatorApplication.class)
class LoanSimulatorApplicationTests {

	@Test
	public void applicationContextLoads() {
	}

	@Test
	void mainMethodStartsApplication() {
		String[] args = {"--server.port=0", "--spring.profiles.active=test"};
		LoanSimulatorApplication.main(args);
	}

}
