package exceptions;

public class TemperaturaInvalidaException extends RuntimeException {
    private String descripcionError;
    public TemperaturaInvalidaException(String error) {
        descripcionError = error;
    }
}
