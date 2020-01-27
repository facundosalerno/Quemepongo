package domain.decision;

import domain.atuendo.Atuendo;
import domain.atuendo.Estado;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="Aceptar")
public class Aceptar extends Decision {
    public Aceptar(){}

    public Aceptar(Atuendo atuendo){
        this.atuendo=atuendo;
        atuendo.cambiarEstado(Estado.ACEPTADO);
    }

    public void deshacer(){
        atuendo.cambiarEstado(Estado.NUEVO);
    }
}
