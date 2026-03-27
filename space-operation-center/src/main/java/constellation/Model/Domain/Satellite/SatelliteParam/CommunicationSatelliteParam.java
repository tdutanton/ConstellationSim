package constellation.Model.Domain.Satellite.SatelliteParam;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CommunicationSatelliteParam extends SatelliteParam {

  private final int bandwidth;

  @JsonCreator
  public CommunicationSatelliteParam(@JsonProperty("type") SatelliteType type,
      @JsonProperty("name") String name,
      @JsonProperty("batteryLevel") double batteryLevel,
      @JsonProperty("bandwidth") int bandwidth) {
    super(type, name, batteryLevel);
    this.bandwidth = bandwidth;
  }
}
