package constellation.Controller;

import constellation.Service.ConstellationService.DTO.ConstellationStatusDTO;
import constellation.Service.SpaceOperationCenterService.AddSatelliteRequest;
import constellation.Service.SpaceOperationCenterService.ConstellationRequest;
import constellation.Service.SpaceOperationCenterService.MissionRequest.MissionRequest;
import constellation.Service.SpaceOperationCenterService.SpaceOperationCenterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<Void> executeMission(@RequestBody MissionRequest request) {
    spaceOperationCenterService.executeMission(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/add-satellites")
  public ResponseEntity<Void> addSatellites(@RequestBody AddSatelliteRequest request) {
    spaceOperationCenterService.addSatellite(request);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/overview")
  public ResponseEntity<List<ConstellationStatusDTO>> overview() {
    List<ConstellationStatusDTO> statuses = spaceOperationCenterService.overview();
    return ResponseEntity.ok(statuses);
  }

  @DeleteMapping("/{constellationName}/satellites/{satelliteName}")
  public ResponseEntity<Void> decommissionSatellite(
      @PathVariable String constellationName,
      @PathVariable String satelliteName) {

    boolean removed = spaceOperationCenterService.deleteSatellite(constellationName,
        satelliteName);

    return removed
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @PostMapping("/activateSatellitesInConstellation")
  public ResponseEntity<Void> activateSatellites(@RequestBody ConstellationRequest request) {
    spaceOperationCenterService.activateSatellites(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/activateAllSatellites")
  public ResponseEntity<Void> activateAllSatellites() {
    spaceOperationCenterService.activateSatellites();
    return ResponseEntity.ok().build();
  }

}
