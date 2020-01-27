package exceptions;

public class NoSePuedenGenerarSugerenciasEx extends RuntimeException {
    String mensaje;
    public NoSePuedenGenerarSugerenciasEx(String mensaje){
        this.mensaje = mensaje;
    }
}
