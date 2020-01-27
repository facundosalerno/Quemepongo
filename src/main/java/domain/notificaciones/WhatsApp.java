package domain.notificaciones;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="WhatsApp")
public class WhatsApp extends MedioDeNotificacion {
    private int numero;

    public WhatsApp(){

    }

    public WhatsApp(int numero){
        this.numero = numero;
    }

    @Override
    public void lanzarNotificacion() {

    }
}
