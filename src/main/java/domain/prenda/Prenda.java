package domain.prenda;

import clima.Clima;
import domain.atuendo.Estado;
import domain.capaPrenda.NivelDeCapa;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import javax.persistence.*;
import java.util.Objects;


@Entity
public class Prenda implements Comparable<Prenda>{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	String nombre;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private TipoDePrenda tipoPrenda;

	@Enumerated(EnumType.STRING)
    private Material material;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "rojo", column = @Column(name = "rojo_color_primario")),
            @AttributeOverride( name = "verde", column = @Column(name = "azul_color_primario")),
            @AttributeOverride( name = "azul", column = @Column(name = "verde_color_primario")),
    })
	private Color colorPrimario;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "rojo", column = @Column(name = "rojo_color_secundario")),
            @AttributeOverride( name = "verde", column = @Column(name = "azul_color_secundario")),
            @AttributeOverride( name = "azul", column = @Column(name = "verde_color_secundario")),
    })
	private Color colorSecundario;

	@Enumerated(EnumType.STRING)
	private Trama trama;

    @Embedded
	private Imagen imagen; //TODO: CHEQUEAR TEMA DE IMAGEN PARA WEB

	@Enumerated(EnumType.STRING)
	private Estado estado;
    

    /** Warning: construir con BorradorPrenda */
    //Solo para que sea compatible con JPA
    protected Prenda() {};

    public Prenda(String nombre,TipoDePrenda tipo, Color colorPrimario, Color colorSecundario, Material material, Trama trama, Imagen imagen) {
        this.nombre=nombre;
        this.tipoPrenda = tipo;
        this.material = material;
        this.colorPrimario = colorPrimario;
        this.colorSecundario = colorSecundario;
        this.trama = trama;
        this.imagen=imagen;
    }





    /** Metodos */

    public void cambiarEstado(Estado estado){
        this.estado = estado;
    }

    public boolean esDeCategoria(Categoria categoria){
        return this.getCategoria()==categoria;
    }

    public boolean abrigaBien(Clima climaActual){
        return tipoPrenda.esAptoParaTemperatura(climaActual);
    }




    /** Getters y setters */

    public Categoria getCategoria() {
        return this.tipoPrenda.categoria();
    }
    public Estado getEstado(){
        return estado;
    }
    public TipoDePrenda getTipoPrenda(){
        return this.tipoPrenda;
    }
    public Material getMaterial(){
        return this.material;
    }
    public Color getColorPrimario() { return colorPrimario;}
    public Color getColorSecundario() { return colorSecundario;}
    public Trama getTrama() { return trama;}
    public NivelDeCapa getNivelDeCapa(){return tipoPrenda.getNivelDeCapa();}
    public Imagen getImagen(){return this.imagen;}

    public String getNombre(){
        return nombre;
    }



    public boolean prendaFueAceptada(){
        return this.getEstado() == Estado.ACEPTADO;
    }


    /** Equals y hashCode */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prenda)) return false;
        Prenda prenda = (Prenda) o;
        return Objects.equals(id, prenda.id) &&
                Objects.equals(tipoPrenda, prenda.tipoPrenda) &&
                material == prenda.material &&
                Objects.equals(colorPrimario, prenda.colorPrimario) &&
                Objects.equals(colorSecundario, prenda.colorSecundario) &&
                trama == prenda.trama &&
                Objects.equals(imagen, prenda.imagen) &&
                estado == prenda.estado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipoPrenda, material, colorPrimario, colorSecundario, trama, imagen, estado);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public int compareTo(Prenda prenda) {
            if(this.getNivelDeCapa() == prenda.getNivelDeCapa()) return 1;
            return 0;
    }
}

