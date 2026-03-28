package constellation.Model.Domain.Satellite.SatelliteParam;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ImagingSatelliteParam extends SatelliteParam {

  private final int resolution;

  @JsonCreator
  public ImagingSatelliteParam(@JsonProperty("type") SatelliteType type,
      @JsonProperty("name") String name,
      @JsonProperty("batteryLevel") double batteryLevel,
      @JsonProperty("resolution") int resolution) {
    super(type, name, batteryLevel);
    this.resolution = resolution;
  }
}
