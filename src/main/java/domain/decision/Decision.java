package domain.decision;

import domain.atuendo.Atuendo;

import javax.persistence.*;
import java.util.Objects;

@Entity
@DiscriminatorColumn(name="tipo_decision")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Decision {
    @Id
    @GeneratedValue
    Long id;

    @OneToOne    
    Atuendo atuendo;

    public abstract void deshacer();





    /** Equals y hashcode */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Decision)) return false;
        Decision decision = (Decision) o;
        return Objects.equals(atuendo, decision.atuendo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atuendo);
    }
}
