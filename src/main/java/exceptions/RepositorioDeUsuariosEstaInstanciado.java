package exceptions;

public class RepositorioDeUsuariosEstaInstanciado extends RuntimeException {
    String error;
    public RepositorioDeUsuariosEstaInstanciado(String error){
        this.error = error;
    }
}
