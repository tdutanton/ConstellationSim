package constellation.Service.ConstellationService.DTO;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

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
