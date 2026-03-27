package constellation.Controller;

import constellation.Service.ConstellationService.ConstellationStatusDTO;
import constellation.Service.SpaceOperationCenterService.AddSatelliteRequest;
import constellation.Service.SpaceOperationCenterService.MissionRequest.MissionRequest;
import constellation.Service.SpaceOperationCenterService.MissionRequest.MissionRequestSatName;
import constellation.Service.SpaceOperationCenterService.SpaceOperationCenterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SpaceOperationController {

  private final SpaceOperationCenterService spaceOperationCenterService;

  @PostMapping("/missions")
  public ResponseEntity<Void> executeMission(@RequestBody MissionRequestSatName request) {
    spaceOperationCenterService.executeMission(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/add-satellites")
  public ResponseEntity<Void> addSatellites(@RequestBody AddSatelliteRequest request) {
    spaceOperationCenterService.addSatellite(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/overview")
  public ResponseEntity<List<ConstellationStatusDTO>> overview() {
    List<ConstellationStatusDTO> statuses = spaceOperationCenterService.overview();
    return ResponseEntity.ok(statuses);
  }

}
