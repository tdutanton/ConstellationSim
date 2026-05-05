package TelemetryService.Service;

import constellationsim.telemetry.proto.TelemetryRequest;
import constellationsim.telemetry.proto.TelemetryServiceGrpc;
import constellationsim.telemetry.proto.TelemetryUpdate;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class TelemetryGrpcService extends TelemetryServiceGrpc.TelemetryServiceImplBase {

  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  private final Random random = new Random();

  private static final List<Long> EMULATED_SATELLITE_IDS = List.of(1L, 2L, 3L);

  public void streamTelemetry(TelemetryRequest request,
      StreamObserver<TelemetryUpdate> responseObserver) {

    if (!(responseObserver instanceof ServerCallStreamObserver<TelemetryUpdate> serverObserver)) {
      responseObserver.onError(new IllegalStateException("Unexpected responseObserver type"));
      return;
    }

    long filterId = request.getSatelliteId();
    boolean sendAll = (filterId == 0);
    List<Long> targetIds = sendAll ? EMULATED_SATELLITE_IDS : List.of(filterId);
    serverObserver.setOnCancelHandler(this::cleanup);

    scheduler.scheduleAtFixedRate(() -> {
      if (serverObserver.isCancelled()) {
        return;
      }

      try {
        for (Long satId : targetIds) {
          if (serverObserver.isCancelled()) {
            return;
          }

          TelemetryUpdate update = TelemetryUpdate.newBuilder()
              .setSatelliteId(satId.intValue())
              .setInsideTemperature(20.0 + random.nextDouble() * 10)
              .setOutsideTemperature(-50.0 + random.nextDouble() * 30)
              .build();

          serverObserver.onNext(update);
        }
      } catch (Exception e) {
        if (!serverObserver.isCancelled()) {
          serverObserver.onError(e);
        }
      }
    }, 0, 2, TimeUnit.SECONDS);
  }

  public void cleanup() {

  }

  @PreDestroy
  public void shutdown() {
    scheduler.shutdown();
    try {
      if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
        scheduler.shutdownNow();
      }
    } catch (InterruptedException e) {
      scheduler.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}
