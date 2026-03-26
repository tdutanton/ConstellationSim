package constellation.Controller;

import constellation.Service.SpaceOperationCenterService.MissionRequest;
import constellation.Service.SpaceOperationCenterService.SpaceOperationCenterService;
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
  public ResponseEntity<Void> executeMission(@RequestBody MissionRequest request) {
    spaceOperationCenterService.executeMission(request);
    return ResponseEntity.ok().build();
  }

}
