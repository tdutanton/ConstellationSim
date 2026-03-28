package constellation.Model.Domain.Satellite.SatelliteParam;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = false)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CommunicationSatelliteParam.class, name = "COMMUNICATION"),
    @JsonSubTypes.Type(value = ImagingSatelliteParam.class, name = "IMAGE")
})
@Getter
public abstract class SatelliteParam {

  protected SatelliteType type;
  protected String name;
  protected double batteryLevel;

  @JsonCreator
  public SatelliteParam(@JsonProperty("type") SatelliteType type,
      @JsonProperty("name") String name,
      @JsonProperty("batteryLevel") double batteryLevel) {
    this.type = type;
    this.name = name;
    this.batteryLevel = batteryLevel;
  }
}
