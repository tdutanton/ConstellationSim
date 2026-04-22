package constellation.Controller;

import constellation.Model.Domain.Satellite.CommunicationSatellite;
import constellation.Model.Domain.Satellite.ImagingSatellite;
import constellation.Repository.DBRepository.CommunicationSatellitesRepository;
import constellation.Repository.DBRepository.ImagingSatellitesRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/satellites")
@RequiredArgsConstructor
public class SatelliteController {

  private final CommunicationSatellitesRepository commRepository;
  private final ImagingSatellitesRepository imagingRepository;

  @GetMapping("/communication")
  public ResponseEntity<List<CommunicationSatellite>> getAllCommunication() {
    return ResponseEntity.ok(commRepository.findAll());
  }

  @GetMapping("/communication/{name}")
  public ResponseEntity<CommunicationSatellite> getCommunicationByName(@PathVariable String name) {
    return commRepository.findByName(name)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/communication")
  public ResponseEntity<CommunicationSatellite> createCommunication(@RequestBody CommunicationSatellite satellite) {
    return ResponseEntity.ok(commRepository.save(satellite));
  }

  @DeleteMapping("/communication/{id}")
  public ResponseEntity<Void> deleteCommunication(@PathVariable Long id) {
    if (commRepository.existsById(id)) {
      commRepository.deleteById(id);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/imaging")
  public ResponseEntity<List<ImagingSatellite>> getAllImaging() {
    return ResponseEntity.ok(imagingRepository.findAll());
  }

  @GetMapping("/imaging/{name}")
  public ResponseEntity<ImagingSatellite> getImagingByName(@PathVariable String name) {
    return imagingRepository.findByName(name)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/imaging")
  public ResponseEntity<ImagingSatellite> createImaging(@RequestBody ImagingSatellite satellite) {
    return ResponseEntity.ok(imagingRepository.save(satellite));
  }

  @DeleteMapping("/imaging/{id}")
  public ResponseEntity<Void> deleteImaging(@PathVariable Long id) {
    if (imagingRepository.existsById(id)) {
      imagingRepository.deleteById(id);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}