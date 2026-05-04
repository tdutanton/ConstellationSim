package TelemetryService.Service;


import constellationsim.telemetry.proto.TelemetryRequest;
import constellationsim.telemetry.proto.TelemetryServiceGrpc;
import constellationsim.telemetry.proto.TelemetryUpdate;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class TelemetryGrpcService extends TelemetryServiceGrpc.TelemetryServiceImplBase {

  private static final List<Long> SATELLITES = List.of(1L, 2L, 3L);
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  private final Random random = new Random();

  @Override
  public void streamTelemetry(TelemetryRequest request,
      StreamObserver<TelemetryUpdate> responseObserver) {

    Long filterId = request.getSatelliteId();
    ServerCallStreamObserver<TelemetryUpdate> serverObserver =
        (ServerCallStreamObserver<TelemetryUpdate>) responseObserver;
    AtomicBoolean isCancelled = new AtomicBoolean(false);
    serverObserver.setOnCancelHandler(() -> {
      isCancelled.set(true);
      System.out.println("Client cancelled the stream");
    });

    Runnable task = () -> {
      if (isCancelled.get()) {
        return;
      }

      try {
        for (Long satId : SATELLITES) {
          if (!filterId.equals(satId)) {
            continue;
          }

          TelemetryUpdate update = TelemetryUpdate.newBuilder()
              .setSatelliteId(satId)
              .setInsideTemperature(20.0 + random.nextDouble() * 10)
              .setOutsideTemperature(-50.0 + random.nextDouble() * 30)
              .build();

          serverObserver.onNext(update);
        }
      } catch (Exception e) {
        serverObserver.onError(e);
      }
    };

    // Отправляется первое обновление сразу, затем каждые 2 секунды
    task.run();
    scheduler.scheduleAtFixedRate(() -> {
      if (!isCancelled.get()) {
        task.run();
      }
    }, 2, 2, TimeUnit.SECONDS);
  }

  private void cleanup() {
  }

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
