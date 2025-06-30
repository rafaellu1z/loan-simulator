package com.creditas.loan.simulator.performance;

import static org.assertj.core.api.Assertions.assertThat;

import com.creditas.loan.simulator.dto.LoanSimulationRequest;
import com.creditas.loan.simulator.dto.LoanSimulationResponse;
import com.creditas.loan.simulator.service.AgeBasedInterestRateCalculator;
import com.creditas.loan.simulator.service.InterestRateCalculator;
import com.creditas.loan.simulator.service.LoanService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(value = "LoanService Load Tests")
@Tag("performance")
public final class LoanServiceLoadTest {

  private LoanService loanService;

  @BeforeEach
  public void setUp() {
    InterestRateCalculator interestRateCalculator = new AgeBasedInterestRateCalculator();
    loanService = new LoanService(interestRateCalculator);
  }

  @Test
  @Order(1)
  @DisplayName("Throughput test - 1000 sequential calculations")
  public void testSequentialThroughput() {
    int numberOfCalculations = 1000;
    List<LoanSimulationRequest> requests = generateRequests(numberOfCalculations);

    long startTime = System.currentTimeMillis();

    List<LoanSimulationResponse> responses = requests.stream()
        .map(loanService::calculateLoanSimulation)
        .toList();

    long endTime = System.currentTimeMillis();
    long totalTime = endTime - startTime;

    assertThat(responses).hasSize(numberOfCalculations);
    assertThat(totalTime).isLessThan(5000); // Max 5 seconds for sequential processing

    double throughput = (numberOfCalculations * 1000.0) / totalTime;
    System.out.printf("Throughput sequential: %.2f calculations/second%n", throughput);

    assertThat(throughput).isGreaterThan(200); // Min 200 calculations/second
  }

  @Test
  @Order(2)
  @DisplayName("Throughput test - 1000 parallel calculations")
  public void testParallelThroughput() {
    int numberOfCalculations = 1000;
    List<LoanSimulationRequest> requests = generateRequests(numberOfCalculations);

    long startTime = System.currentTimeMillis();

    List<LoanSimulationResponse> responses = requests.parallelStream()
        .map(loanService::calculateLoanSimulation)
        .toList();

    long endTime = System.currentTimeMillis();
    long totalTime = endTime - startTime;

    assertThat(responses).hasSize(numberOfCalculations);
    assertThat(totalTime).isLessThan(2000); // Max 2 seconds for parallel processing

    double throughput = (numberOfCalculations * 1000.0) / totalTime;
    System.out.printf("Parallel Throughput: %.2f calculations/second%n", throughput);

    assertThat(throughput).isGreaterThan(500); // Min 500 calculations/second
  }

  @Test
  @Order(3)
  @DisplayName("Stress test - 10000 calculations with multiple threads")
  public void testStressWithMultipleThreads() throws InterruptedException {
    int totalCalculations = 10000;
    int numberOfThreads = 10;
    int calculationsPerThread = totalCalculations / numberOfThreads;

    ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfThreads);
    AtomicInteger successCount = new AtomicInteger(0);
    AtomicInteger errorCount = new AtomicInteger(0);

    long startTime = System.currentTimeMillis();

    for (int i = 0; i < numberOfThreads; i++) {
      executor.submit(() -> {
        try {
          List<LoanSimulationRequest> requests = generateRequests(calculationsPerThread);

          for (LoanSimulationRequest request : requests) {
            try {
              LoanSimulationResponse response = loanService.calculateLoanSimulation(request);
              assertThat(response).isNotNull();
              successCount.incrementAndGet();
            } catch (Exception e) {
              errorCount.incrementAndGet();
            }
          }
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await(30, TimeUnit.SECONDS);
    executor.shutdown();

    long endTime = System.currentTimeMillis();
    long totalTime = endTime - startTime;

    double throughput = (successCount.get() * 1000.0) / totalTime;
    double errorRate = (errorCount.get() * 100.0) / totalCalculations;

    System.out.printf("Stress Test Results:%n");
    System.out.printf("- Total time: %d ms%n", totalTime);
    System.out.printf("- Successful calculations: %d%n", successCount.get());
    System.out.printf("- Failed calculations: %d%n", errorCount.get());
    System.out.printf("- Throughput: %.2f cálculos/segundo%n", throughput);
    System.out.printf("- Error rate: %.2f%%%n", errorRate);

    assertThat(errorRate).isLessThan(1.0); // Max 1% error rate
    assertThat(throughput).isGreaterThan(300); // Min 300 calculations/second
  }

  @Test
  @Order(4)
  @DisplayName("Memory test - verify leakage and usage")
  public void testMemoryUsage() {
    Runtime runtime = Runtime.getRuntime();

    // Force garbage collection before starting the test
    System.gc();
    long initialMemory = runtime.totalMemory() - runtime.freeMemory();

    // Perform a large number of calculations to check memory usage
    for (int i = 0; i < 5000; i++) {
      LoanSimulationRequest request = generateSingleRequest();
      loanService.calculateLoanSimulation(request);
    }

    // Force garbage collection again
    System.gc();
    long finalMemory = runtime.totalMemory() - runtime.freeMemory();

    long memoryIncrease = finalMemory - initialMemory;
    long memoryIncreaseInMB = memoryIncrease / (1024 * 1024);

    System.out.printf("Memory usage increase: %d MB%n", memoryIncreaseInMB);

    // Verify that memory increase is within acceptable limits
    assertThat(memoryIncreaseInMB).isLessThan(50); // Max 50 MB increase expected
  }

  private List<LoanSimulationRequest> generateRequests(final int count) {
    return IntStream.range(0, count)
        .mapToObj(i -> generateSingleRequest())
        .toList();
  }

  private LoanSimulationRequest generateSingleRequest() {
    Random random = new Random();
    return new LoanSimulationRequest(
        "client-" + random.nextInt(1000),
        new BigDecimal(5000 + random.nextInt(95000)).setScale(2, RoundingMode.HALF_UP),
        LocalDate.now().minusYears(20 + random.nextInt(50)),
        6 + random.nextInt(54)
    );
  }
}
