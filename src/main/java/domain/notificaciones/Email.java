package domain.notificaciones;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="Email")
public class Email extends MedioDeNotificacion {
    private String email;

    public Email(String email){
        this.email = email;
    }

    public Email(){

    }

    @Override
    public void lanzarNotificacion() {

    }
}
