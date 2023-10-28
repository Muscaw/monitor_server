package dev.muscaw.monitor.util.http;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class Retryable {

  public static <T> T retry(Supplier<CompletableFuture<T>> retryableQuery, int retryCount) {
    if (retryCount <= 0) {
      throw new RuntimeException("Could not retry less than 0 times");
    }
    for (int retriesLeft = retryCount - 1; retriesLeft >= 0; retriesLeft--) {
      CompletableFuture<T> res = retryableQuery.get();
      try {
        return res.get();
      } catch (InterruptedException | ExecutionException e) {
        if (retriesLeft == 0) {
          throw new RuntimeException(e);
        }
      }
    }
    // This code should never be called as it should return or raise within the loop
    throw new RuntimeException("No more retries left");
  }
}
