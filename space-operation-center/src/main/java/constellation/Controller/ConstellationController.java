package constellation.Controller;

import constellation.Model.Domain.Constellation.SatelliteConstellation;
import constellation.Repository.DBRepository.ConstellationsRepository;
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
@RequestMapping("/api/constellations")
@RequiredArgsConstructor
public class ConstellationController {

  private final ConstellationsRepository repository;

  @GetMapping
  public ResponseEntity<List<SatelliteConstellation>> getAll() {
    return ResponseEntity.ok(repository.findAll());
  }

  @GetMapping("/{name}")
  public ResponseEntity<SatelliteConstellation> getByName(@PathVariable String name) {
    return repository.findByConstellationName(name)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<SatelliteConstellation> create(@RequestBody SatelliteConstellation constellation) {
    return ResponseEntity.ok(repository.save(constellation));
  }

  @PutMapping("/{id}")
  public ResponseEntity<SatelliteConstellation> update(
      @PathVariable Long id,
      @RequestBody SatelliteConstellation constellation) {
    return repository.findById(id)
        .map(existing -> {
          existing.setConstellationName(constellation.getConstellationName());
          return ResponseEntity.ok(repository.save(existing));
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (repository.existsById(id)) {
      repository.deleteById(id);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}