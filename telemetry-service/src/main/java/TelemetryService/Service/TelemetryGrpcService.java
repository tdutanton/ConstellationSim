package TelemetryService.Service;

import TelemetryService.Kafka.SatelliteRegistry;
import constellationsim.telemetry.proto.TelemetryRequest;
import constellationsim.telemetry.proto.TelemetryServiceGrpc;
import constellationsim.telemetry.proto.TelemetryUpdate;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PreDestroy;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class TelemetryGrpcService extends TelemetryServiceGrpc.TelemetryServiceImplBase {

  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  private final Random random = new Random();
  private final SatelliteRegistry satelliteRegistry;

  public void streamTelemetry(TelemetryRequest request,
      StreamObserver<TelemetryUpdate> responseObserver) {

    if (!(responseObserver instanceof ServerCallStreamObserver<TelemetryUpdate> serverObserver)) {
      responseObserver.onError(new IllegalStateException("Unexpected responseObserver type"));
      return;
    }

    long filterId = request.getSatelliteId();
    boolean sendAll = (filterId == 0);

    serverObserver.setOnCancelHandler(this::cleanup);

    scheduler.scheduleAtFixedRate(() -> {
      if (serverObserver.isCancelled()) {
        return;
      }

      try {
        Set<Long> targetIds = resolveTargetIds(filterId);
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

  private Set<Long> resolveTargetIds(long filterId) {
    if (filterId == 0) {
      // Запрос "всех": возвращаем только активные из реестра
      return satelliteRegistry.getActiveIds();
    } else {
      // Запрос конкретного: проверяем наличие в реестре
      return satelliteRegistry.contains(filterId) ? Set.of(filterId) : Set.of();
    }
  }
}
