package com.creditas.loan.simulator.performance;

import static org.assertj.core.api.Assertions.assertThat;

import com.creditas.loan.simulator.dto.LoanSimulationRequest;
import com.creditas.loan.simulator.dto.LoanSimulationResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("performance")
public final class LoanControllerPerformanceTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  @Order(1)
  @DisplayName("Performance Test API - 1000 requests with 100 threads")
  public void testApiPerformance() throws InterruptedException {
    int numberOfRequests = 100;
    int numberOfThreads = 10;

    ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfRequests);
    AtomicInteger successCount = new AtomicInteger(0);
    AtomicLong totalResponseTime = new AtomicLong(0);

    long startTime = System.currentTimeMillis();

    for (int i = 0; i < numberOfRequests; i++) {
      executor.submit(() -> {
        try {
          long requestStart = System.currentTimeMillis();

          LoanSimulationRequest request = new LoanSimulationRequest(
              "client-123",
              new BigDecimal("50000"),
              LocalDate.now().minusYears(30),
              24
          );

          ResponseEntity<LoanSimulationResponse> response = restTemplate.postForEntity(
              "/v1/loans/simulate", request, LoanSimulationResponse.class
          );

          long requestEnd = System.currentTimeMillis();
          totalResponseTime.addAndGet(requestEnd - requestStart);

          if (response.getStatusCode().is2xxSuccessful()) {
            successCount.incrementAndGet();
          }
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await(60, TimeUnit.SECONDS);
    executor.shutdown();

    long endTime = System.currentTimeMillis();
    long totalTime = endTime - startTime;

    double throughput = (successCount.get() * 1000.0) / totalTime;
    double averageResponseTime = totalResponseTime.get() / (double) successCount.get();

    System.out.printf("API Performance Results:%n");
    System.out.printf("- Successful requests: %d/%d%n", successCount.get(), numberOfRequests);
    System.out.printf("- Total time: %d ms%n", totalTime);
    System.out.printf("- Throughput: %.2f requests/second%n", throughput);
    System.out.printf("- Average response time: %.2f ms%n", averageResponseTime);

    assertThat(successCount.get()).isEqualTo(numberOfRequests);
    assertThat(averageResponseTime).isLessThan(100); // Max 100 ms for each request
    assertThat(throughput).isGreaterThan(10); // Min 10 requests/second
  }
}
