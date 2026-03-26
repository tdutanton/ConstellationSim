package constellation.Service.SpaceOperationCenterService;

import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AddSatelliteRequest {

  private final String constellationName;
  private final List<SatelliteParam> satelliteParams;
}
