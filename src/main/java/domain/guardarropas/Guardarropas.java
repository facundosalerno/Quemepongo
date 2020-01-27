package domain.guardarropas;

import clima.Clima;
import clima.Meteorologo;
import com.google.common.collect.Comparators;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import domain.atuendo.Atuendo;
import domain.capaPrenda.CapasPorTemperatura;
import domain.capaPrenda.NivelDeCapa;
import domain.prenda.Categoria;
import domain.prenda.Prenda;
import domain.usuario.TipoDeUsuario;
import exceptions.NoPerteneceALaCategoriaException;
import exceptions.NoSePuedenGenerarSugerenciasEx;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Entity
@DiscriminatorColumn(name = "tipo_guardarropas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Guardarropas{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "guardarropas_superiores_id")
    protected List<Prenda> prendasSuperiores = new ArrayList<Prenda>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "guardarropas_inferiores_id")
    protected List<Prenda> prendasInferiores = new ArrayList<Prenda>();
    ;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "guardarropas_calzados_id")
    protected List<Prenda> calzados = new ArrayList<Prenda>();
    ;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "guardarropas_accesorios_id")
    protected List<Prenda> accesorios = new ArrayList<Prenda>();
    ;

    public String getNombre() {
        return nombre;
    }

    String nombre;


    /**
     * Abstract
     */

    public abstract TipoDeUsuario tipoDeUsuarioQueAcepta();

    /**
     * Metodos
     */

    public List<Prenda> getPrendas() {
        List<Prenda> todasLasPrendas = new ArrayList(prendasSuperiores);
        todasLasPrendas.addAll(prendasInferiores);
        todasLasPrendas.addAll(calzados);
        todasLasPrendas.addAll(accesorios);
        return todasLasPrendas;
    }


    public void prendasCoincidenConCategoria(List<Prenda> prendas, Categoria categoria) {
        if (!prendas.stream().allMatch(prenda -> prenda.esDeCategoria(categoria))) {
            throw new NoPerteneceALaCategoriaException();
        }
    }

    public List<Atuendo> sugerirAtuendo(Meteorologo meteorologo) {
        return this.sugerirAtuendo(meteorologo,0);
    }

    public List<Atuendo> sugerirAtuendo(Meteorologo meteorologo , double coeficienteUsuario) {
        Clima climaActual = meteorologo.obtenerClima();

        climaActual.setTemperature(climaActual.getTemperature() + coeficienteUsuario * 2);

        return Sets.cartesianProduct(ImmutableList.of(ImmutableSet.copyOf(superponerPrendas(prendasSuperiores, climaActual)), ImmutableSet.copyOf(obtenerPrendasParaClima(prendasInferiores, climaActual)), ImmutableSet.copyOf(obtenerPrendasParaClima(calzados, climaActual)), ImmutableSet.copyOf(obtenerPrendasParaClima(accesorios, climaActual))))
                .stream()
                .map(list -> new Atuendo((List<Prenda>) list.get(0),(Prenda) list.get(1), (Prenda) list.get(2),(Prenda) list.get(3)))
                .filter(atuendo -> atuendo.esElegible())
                .collect(Collectors.toList());
    }


    public static List<List<Prenda>> superponerPrendas(List<Prenda> prendas, Clima climaActual) {  //TODO: IMPORTANTE private
        int cantidadCapas = CapasPorTemperatura.capasDeAbrigoParaClima(climaActual);
        if (prendas.size() < cantidadCapas) { /* Fix para: java.lang.IllegalArgumentException: size (2) must be <= set.size() (1) */
            throw new NoSePuedenGenerarSugerenciasEx("No hay suficientes prendas en el guardarropas para satisfacer el clima actual");
        }

        return Sets.combinations(ImmutableSet.copyOf(prendas), CapasPorTemperatura.capasDeAbrigoParaClima(climaActual))//Genera combinatorias con la lista de prendas a partir de la cantidadCapas calculada
                //Tipo Set<Set<Prenda>>
                .stream()
                .map(listaPrenda -> listaPrenda.stream().sorted(Comparator.comparing(Prenda::getNivelDeCapa)).collect(Collectors.toList()))
                .filter(listaPrenda -> estaOrdenada(listaPrenda))
                .collect(Collectors.toList());


    }

    private List<Prenda> obtenerPrendasParaClima(List<Prenda> prendas, Clima climaActual) {
        return prendas.stream().filter(prenda -> prenda.abrigaBien(climaActual)).collect(Collectors.toList());
    }

    public static boolean estaOrdenada(List<Prenda> listaDePrendas) {
        if (listaDePrendas.get(0).getNivelDeCapa() != NivelDeCapa.ABAJO) return false;
        return Comparators.isInOrder(listaDePrendas, Comparator.<Prenda>naturalOrder());
    }

    public static boolean abrigaBien(List<Prenda> listaDePrendas, Clima climaActual) {
        return listaDePrendas.stream().allMatch(capa -> capa.abrigaBien(climaActual));
    }


    //TODO importante hacer estos metodos abstract para que los implemente cada clase
    public void agregarPrendaSuperior(Prenda prendaAgregada) {
        if (!prendaAgregada.esDeCategoria(Categoria.PARTE_SUPERIOR)) {
            throw new NoPerteneceALaCategoriaException();
        }

        this.prendasSuperiores.add(prendaAgregada);

    }

    public void agregarPrendaInferior(Prenda prendaAgregada) {
        if (!prendaAgregada.esDeCategoria(Categoria.PARTE_INFERIOR)) {
            throw new NoPerteneceALaCategoriaException();
        }

        this.prendasInferiores.add(prendaAgregada);
    }

    public void agregarPrendaCalzado(Prenda prendaAgregada) {
        if (!prendaAgregada.esDeCategoria(Categoria.CALZADO)) {
            throw new NoPerteneceALaCategoriaException();
        }

        this.calzados.add(prendaAgregada);
    }

    public void agregarPrendaAccesorio(Prenda prendaAgregada) {
        if (!prendaAgregada.esDeCategoria(Categoria.ACCESORIOS)) {
            throw new NoPerteneceALaCategoriaException();
        }

        this.accesorios.add(prendaAgregada);
    }

    public void agregarPrenda(Prenda prenda){
        switch (prenda.getCategoria()){
            case PARTE_SUPERIOR:
                prendasSuperiores.add(prenda);
                break;
            case PARTE_INFERIOR:
                prendasInferiores.add(prenda);
                break;
            case CALZADO:
                calzados.add(prenda);
                break;
            case ACCESORIOS:
                accesorios.add(prenda);
                break;
        }
    }

    // getters y setters

    public Long getId() {
        return id;
    }

    public List<Prenda> getPrendasSuperiores() {
        return prendasSuperiores;
    }

    public List<Prenda> getPrendasInferiores() {
        return prendasInferiores;
    }

    public List<Prenda> getCalzados() {
        return calzados;
    }

    public List<Prenda> getAccesorios() {
        return accesorios;
    }

    /**
     * Equals y hashcode
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guardarropas)) return false;
        Guardarropas that = (Guardarropas) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(prendasSuperiores, that.prendasSuperiores) &&
                Objects.equals(prendasInferiores, that.prendasInferiores) &&
                Objects.equals(calzados, that.calzados) &&
                Objects.equals(accesorios, that.accesorios) &&
                Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prendasSuperiores, prendasInferiores, calzados, accesorios, nombre);
    }
}
