package constellation.Client;

import constellation.Repository.SatellitesRepository;
import constellationsim.telemetry.proto.TelemetryRequest;
import constellationsim.telemetry.proto.TelemetryServiceGrpc;
import constellationsim.telemetry.proto.TelemetryUpdate;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TelemetryGrpcClient {

  private final SatellitesRepository repository;

  @GrpcClient("telemetry-service")
  private TelemetryServiceGrpc.TelemetryServiceStub telemetryStub;

  @PostConstruct
  public void startStreaming() {

    TelemetryRequest request = TelemetryRequest.newBuilder()
        .setSatelliteId(0)
        .build();

    StreamObserver<TelemetryUpdate> responseObserver = new StreamObserver<>() {
      @Override
      public void onNext(TelemetryUpdate telemetryUpdate) {
        try {
          repository.updateTemperatures(
              telemetryUpdate.getSatelliteId(),
              telemetryUpdate.getOutsideTemperature(),
              telemetryUpdate.getInsideTemperature()
          );
        } catch (Exception e) {
          System.out.println(e);
        }
      }

      @Override
      public void onError(Throwable throwable) {
        try {
          Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
        startStreaming();
      }

      @Override
      public void onCompleted() {
        System.out.println("gRPC stream completed unexpectedly, reconnecting...");
        startStreaming();
      }
    };

    telemetryStub.streamTelemetry(request, responseObserver);
  }

}
