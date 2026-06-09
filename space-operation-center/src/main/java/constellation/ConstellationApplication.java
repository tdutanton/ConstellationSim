package constellation;

import constellation.Model.Domain.Constellation.SatelliteConstellation;
import constellation.Model.Domain.Satellite.Satellite;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
public class ConstellationApplication {

  public static void main(String[] args) {
    Satellite.setConsolePrintMode(true);
    SatelliteConstellation.setConsolePrintMode(true);
    SpringApplication.run(ConstellationApplication.class, args);
  }
}
