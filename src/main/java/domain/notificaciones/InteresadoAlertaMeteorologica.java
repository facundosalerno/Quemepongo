package domain.notificaciones;

import clima.Clima;

public interface InteresadoAlertaMeteorologica {
    void recibirNotificacionAlertaMeteorologica(Clima climaActual);
}
