package domain.decision;

import domain.atuendo.Atuendo;
import domain.atuendo.Estado;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="Calificar")
public class Calificar extends Decision{

    public Calificar(){}

    public Calificar(Atuendo atuendo, int calificacion){
        this.atuendo=atuendo;
        atuendo.calificar(calificacion);
        atuendo.cambiarEstado(Estado.CALIFICADO);
    }

    @Override
    public void deshacer() {
        atuendo.cambiarEstado(Estado.ACEPTADO);
    }
}
