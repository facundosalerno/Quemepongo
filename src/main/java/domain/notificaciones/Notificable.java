package domain.notificaciones;

import domain.usuario.Usuario;

public interface Notificable {
    void agregarNotificado(Usuario interesado);
    void eliminarNotificado(Usuario interesado);
    void notificar();
}
