package constellation.Controller.DBController;

import constellation.Model.Domain.Constellation.SatelliteConstellation;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Service.ConstellationService.ServiceDB.ConstellationServiceDB;
import constellation.Service.SatelliteService.Impl.SatelliteServiceDB;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/db")
public class DBController {

  private final ConstellationServiceDB constellationServiceDB;
  private final SatelliteServiceDB satelliteServiceDB;

  public DBController(ConstellationServiceDB constellationServiceDB,
      SatelliteServiceDB satelliteServiceDB) {
    this.constellationServiceDB = constellationServiceDB;
    this.satelliteServiceDB = satelliteServiceDB;
  }

  @PostMapping("/create-satellites")
  public ResponseEntity<Satellite> createSatellite(@RequestBody SatelliteParam param) {
    return ResponseEntity.ok(satelliteServiceDB.createSatellite(param));
  }

  @PostMapping("/constellations/{constellationId}/{satelliteId}")
  public ResponseEntity<Satellite> addSatellite(
      @PathVariable Long constellationId,
      @PathVariable Long satelliteId) {

    Satellite satellite = satelliteServiceDB.findById(satelliteId)
        .orElseThrow(() -> new IllegalArgumentException("Satellite not found: " + satelliteId));

    SatelliteConstellation constellation = constellationServiceDB.findById(constellationId)
        .orElseThrow(
            () -> new IllegalArgumentException("Constellation not found: " + constellationId));

    constellationServiceDB.addSatelliteToConstellation(
        constellation.getConstellationName(), satellite);

    return ResponseEntity.ok(satellite);
  }

}