package domain.notificaciones;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="SMS")
public class SMS extends MedioDeNotificacion {
    private int numero;

    public SMS(int numero){
        this.numero = numero;
    }

    public SMS(){

    }

    @Override
    public void lanzarNotificacion() {

    }
}
