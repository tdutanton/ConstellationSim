package constellation.Service.ConstellationService;

import constellation.Model.Domain.Satellite.Satellite;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ConstellationStatusDTO {

  private String constellationName;
  private int satellitesCount;
  @Getter
  @Setter
  private List<String> satellitesStatus;
}
