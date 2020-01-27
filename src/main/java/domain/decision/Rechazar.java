package domain.decision;

import domain.atuendo.Atuendo;
import domain.atuendo.Estado;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(value="Rechazar")
public class Rechazar extends Decision{

    public Rechazar(){}

    public Rechazar (Atuendo atuendo){
        this.atuendo=atuendo;
        atuendo.cambiarEstado(Estado.RECHAZADO);
    }

    public void deshacer(){
        atuendo.cambiarEstado(Estado.NUEVO);
    }
}
