package constellation.Service.SatelliteService.Impl;

import constellation.Model.Domain.Internal.Exception.SpaceOperationException;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Model.Factory.SatelliteFactory.SatelliteFactory;
import constellation.Service.SatelliteService.SatelliteService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SatelliteServiceImpl implements SatelliteService {

  private final List<SatelliteFactory> factories;

  @Override
  public Satellite createSatellite(SatelliteParam param) throws SpaceOperationException {
    for (SatelliteFactory factory : factories) {
      if (factory.isSatelliteTypeSupported(param.getType())) {
        try {
          return factory.createSatelliteWithParameter(param);
        } catch (SpaceOperationException e) {
          System.out.printf("Ошибка создания спутника: %s%n", e);
        }
      }
    }
    throw new SpaceOperationException(
        String.format("Тип спутника %s не поддерживается сервисом",
            param.getType()));
  }
}

