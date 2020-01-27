package domain.prenda;

import clima.Clima;
import domain.capaPrenda.NivelDeCapa;
import domain.temperaturaPrenda.RangoTemperatura;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
public class TipoDePrenda {
	@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Enumerated(EnumType.STRING)
    private Categoria categoria;


	//TODO: VERIFICAR
	@ElementCollection
	@CollectionTable(name = "MATERIALES_VALIDOS", joinColumns = @JoinColumn(name = "tipoPrenda_id"))
    @Column(name = "material",columnDefinition="VARCHAR(40)")
    @Enumerated(EnumType.STRING)
    private List<Material> materialesValidos;

    @Embedded
    private RangoTemperatura temperaturaSoportada;

	@Enumerated(EnumType.STRING)
    private NivelDeCapa nivelDeCapa;

	//Solo para que sea compatible con JPA
    protected TipoDePrenda() {}

    public TipoDePrenda(long id,Categoria categoria, List<Material> materiales, RangoTemperatura temperatura, NivelDeCapa nivelDeCapa) {
        this.id=id;
        this.categoria = categoria;
        this.materialesValidos = materiales;
        this.temperaturaSoportada = temperatura;
        this.nivelDeCapa=nivelDeCapa;
    }





    /** Metodos */

    public NivelDeCapa getNivelDeCapa(){
        return this.nivelDeCapa;
    }

    public Categoria categoria() {
        return this.categoria;
    }

    public boolean permiteMaterial(Material material) {
        return materialesValidos.contains(material);
    }

    public boolean esAptoParaTemperatura(Clima climaActual){
        return temperaturaSoportada.seAdapta(climaActual);
    }





    /** Atributos estaticos de prueba */

    public static final TipoDePrenda SIN_ACCESORIO = new TipoDePrenda(1,Categoria.ACCESORIOS, Arrays.asList(Material.NINGUNO), new RangoTemperatura(-20, 40), NivelDeCapa.ABAJO);
    public static final TipoDePrenda ZAPATO = new TipoDePrenda(2,Categoria.CALZADO, Arrays.asList(Material.CUERO, Material.GAMUZA), new RangoTemperatura(10, 25), NivelDeCapa.MEDIO);
    public static final TipoDePrenda REMERA = new TipoDePrenda(3,Categoria.PARTE_SUPERIOR, Arrays.asList(Material.ALGODON), new RangoTemperatura(-20, 40), NivelDeCapa.ABAJO);
    public static final TipoDePrenda CAMISA = new TipoDePrenda(4,Categoria.PARTE_SUPERIOR, Arrays.asList(Material.ALGODON,Material.POLIESTER, Material.JEAN,Material.LINO,Material.SEDA), new RangoTemperatura(1, 30), NivelDeCapa.ABAJO);
    public static final TipoDePrenda PANTALON = new TipoDePrenda(5,Categoria.PARTE_INFERIOR, Arrays.asList(Material.JEAN,Material.POLIESTER), new RangoTemperatura(-5, 28), NivelDeCapa.MEDIO);
    public static final TipoDePrenda SHORT = new TipoDePrenda(6,Categoria.PARTE_INFERIOR, Arrays.asList(Material.ALGODON,Material.JEAN), new RangoTemperatura(20, 40), NivelDeCapa.MEDIO);
    public static final TipoDePrenda BLUSA = new TipoDePrenda(7,Categoria.PARTE_SUPERIOR, Arrays.asList(Material.ALGODON), new RangoTemperatura(10, 25), NivelDeCapa.MEDIO);
    public static final TipoDePrenda ZAPATILLA = new TipoDePrenda(8,Categoria.CALZADO, Arrays.asList(Material.GAMUZA), new RangoTemperatura(-20, 40), NivelDeCapa.MEDIO);
    public static final TipoDePrenda POLLERA = new TipoDePrenda(9,Categoria.PARTE_INFERIOR, Arrays.asList(Material.POLIESTER), new RangoTemperatura(20, 30), NivelDeCapa.MEDIO);
    public static final TipoDePrenda OJOTAS = new TipoDePrenda(10,Categoria.CALZADO, Arrays.asList(Material.GOMA), new RangoTemperatura(25, 40), NivelDeCapa.ABAJO);
    public static final TipoDePrenda ALPARGATAS = new TipoDePrenda(11,Categoria.CALZADO, Arrays.asList(Material.ALGODON), new RangoTemperatura(15, 30), NivelDeCapa.MEDIO);
    public static final TipoDePrenda PULSERA = new TipoDePrenda(12,Categoria.ACCESORIOS, Arrays.asList(Material.PLASTICO), new RangoTemperatura(-20, 40), NivelDeCapa.ABAJO);
    public static final TipoDePrenda ANTEOJOS = new TipoDePrenda(13,Categoria.ACCESORIOS, Arrays.asList(Material.PLASTICO), new RangoTemperatura(-20, 40), NivelDeCapa.ABAJO);
    public static final TipoDePrenda BUSO = new TipoDePrenda(14,Categoria.PARTE_SUPERIOR, Arrays.asList(Material.ALGODON), new RangoTemperatura(5, 25), NivelDeCapa.MEDIO);
    public static final TipoDePrenda SWEATER = new TipoDePrenda(15,Categoria.PARTE_SUPERIOR, Arrays.asList(Material.LINO), new RangoTemperatura(5, 25), NivelDeCapa.MEDIO);
    public static final TipoDePrenda CAMPERA = new TipoDePrenda(16,Categoria.PARTE_SUPERIOR, Arrays.asList(Material.GABARDINA,Material.JEAN, Material.ALGODON, Material.PLUMA), new RangoTemperatura(5, 25), NivelDeCapa.ARRIBA);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TipoDePrenda)) return false;
        TipoDePrenda that = (TipoDePrenda) o;
        return categoria == that.categoria &&
                Objects.equals(materialesValidos, that.materialesValidos) &&
                Objects.equals(temperaturaSoportada, that.temperaturaSoportada) &&
                nivelDeCapa == that.nivelDeCapa;
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoria, materialesValidos, temperaturaSoportada, nivelDeCapa);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}