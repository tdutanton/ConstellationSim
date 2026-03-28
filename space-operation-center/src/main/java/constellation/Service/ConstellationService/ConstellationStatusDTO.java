package constellation.Service.ConstellationService;

import constellation.Model.Domain.Satellite.Satellite;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
public class ConstellationStatusDTO {

  private String constellationName;
  private int satellitesCount;
  private List<String> satellitesStatus;

  public ConstellationStatusDTO() {
    this.satellitesStatus = new ArrayList<>();
  }
}
