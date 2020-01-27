package domain.atuendo;

import clima.Clima;
import domain.prenda.Categoria;
import domain.prenda.Prenda;
import exceptions.AtuendoInvalidoException;
import exceptions.NoCumpleRequisitoParaCalificarException;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Atuendo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @OneToMany
    @JoinColumn(name = "atuendo_id")
    private List<Prenda> prendasSuperiores=new ArrayList<>();

    @OneToOne(cascade=CascadeType.ALL)
    private Prenda prendaInferior;

    @OneToOne(cascade=CascadeType.ALL)
    private Prenda calzado;

    @OneToOne(cascade=CascadeType.ALL)
    private Prenda accesorio;


    @Enumerated
    private Estado estado;

    private int calificacion;

    public Atuendo(){}

    public Atuendo(List<Prenda> prendasSuperiores, Prenda prendaInferior, Prenda calzado, Prenda accesorio) {
        if (!atuendoEsValido(prendasSuperiores, prendaInferior, calzado, accesorio))
            throw new AtuendoInvalidoException();
        this.prendasSuperiores = prendasSuperiores;
        this.prendaInferior = prendaInferior;
        this.calzado = calzado;
        this.accesorio = accesorio;
        estado = Estado.NUEVO;
    }


    /**
     * Metodos
     */

    /* El estado debe cambiar tambien para sus componentes ya que en base a eso, un atuendo podria ser o no elegible */
    public void cambiarEstado(Estado estado) {
        this.estado = estado;
        this.prendasSuperiores.forEach(prenda -> prenda.cambiarEstado(estado));
        this.prendaInferior.cambiarEstado(estado);
        this.calzado.cambiarEstado(estado);
        this.accesorio.cambiarEstado(estado);
    }

    public boolean atuendoEsValido(List<Prenda> prendaSuperior, Prenda prendaInferior, Prenda calzado, Prenda accesorio) {
        return (prendaSuperior.stream().allMatch(prenda-> esCategoria(prenda, Categoria.PARTE_SUPERIOR)) && esCategoria(prendaInferior, Categoria.PARTE_INFERIOR)
                && esCategoria(calzado, Categoria.CALZADO) && esCategoria(accesorio, Categoria.ACCESORIOS));

    }

    /* Si un usuario acepta el atuendo, entonces acepta todas sus prendas (capas) y ya no sera elegible por otro usuario que comparta el mismo guardarropas */
    public boolean esElegible() {
        return this.estado != Estado.ACEPTADO && ningunaPrendaFueAceptada();
    }

    private boolean ningunaPrendaFueAceptada() {
        return !prendasSuperiores.stream().allMatch(prenda -> prenda.prendaFueAceptada()) && !prendaInferior.prendaFueAceptada() && !calzado.prendaFueAceptada() && !accesorio.prendaFueAceptada();
    }

    public boolean esCategoria(Prenda prenda, Categoria categoria) {
        return (prenda.getCategoria() == categoria);
    }

    public void calificar(int calificacion) {        //Solo se usa al momento de generar una decision de calificar
        if (this.estado == Estado.ACEPTADO) {
            this.calificacion = calificacion > 5 ? 5 : calificacion < -5 ? -5 : calificacion;
        }else{
            throw new NoCumpleRequisitoParaCalificarException();
        }

    }

    public boolean revalidadAtuendo(Clima climaActual){
        return prendasSuperiores.stream().allMatch(prenda -> prenda.abrigaBien(climaActual)) &&
                prendaInferior.abrigaBien(climaActual) &&
                calzado.abrigaBien(climaActual) &&
                accesorio.abrigaBien(climaActual);
    }

    /**
     * Getters y setters
     */

    public Estado getEstado() {
        return this.estado;
    }

    public Prenda getPrendaInferior() {
        return prendaInferior;
    }

    public List<Prenda> getPrendasSuperiores() {
        return prendasSuperiores;
    }

    public Prenda getAccesorio() {
        return accesorio;
    }

    public Prenda getCalzado() {
        return calzado;
    }

    public Long getId() {
        return id;
    }

    public int getCalificacion() {
        return calificacion;
    }


    /**
     * Equals y hashcode
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atuendo)) return false;
        Atuendo atuendo = (Atuendo) o;
        return calificacion == atuendo.calificacion &&
                Objects.equals(prendasSuperiores, atuendo.prendasSuperiores) &&
                Objects.equals(prendaInferior, atuendo.prendaInferior) &&
                Objects.equals(calzado, atuendo.calzado) &&
                Objects.equals(accesorio, atuendo.accesorio) &&
                estado == atuendo.estado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(prendasSuperiores, prendaInferior, calzado, accesorio, estado, calificacion);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
