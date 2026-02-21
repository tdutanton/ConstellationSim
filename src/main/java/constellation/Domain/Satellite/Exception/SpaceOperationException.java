package constellation.Domain.Satellite.Exception;

public class SpaceOperationException extends Exception {

  public SpaceOperationException(String message) {
    super(message);
  }

  public SpaceOperationException(String message, Throwable cause) {
    super(message, cause);
  }
}
