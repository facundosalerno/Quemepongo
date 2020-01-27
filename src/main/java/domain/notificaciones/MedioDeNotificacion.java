package domain.notificaciones;

import javax.persistence.*;

@Entity
@DiscriminatorColumn(name="tipo_medioDeNotificacion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class MedioDeNotificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public abstract void lanzarNotificacion();

}
