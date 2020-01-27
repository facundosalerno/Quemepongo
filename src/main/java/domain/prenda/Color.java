package domain.prenda;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Color {
    private int  rojo, verde, azul;
    //podria hacer que el color primario y el secundario sean atributos de color y  los defino con metodos en los que ingreso rojo verde y azul

    //Solo para que sea compatible con JPA
    protected Color() {};
    public Color (int rojo, int verde, int azul){
        this.rojo=rojo;
        this.verde=verde;
        this.azul=azul;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return rojo == color.rojo &&
                verde == color.verde &&
                azul == color.azul;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rojo, verde, azul);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
