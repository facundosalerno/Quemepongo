package domain.evento;

import clima.Meteorologo;
import domain.atuendo.Atuendo;
import domain.notificaciones.Notificable;
import domain.usuario.Usuario;
import exceptions.NoHaySugerenciasParaElEvento;
import exceptions.TodaviaNoEstaCercaElEvento;
import org.uqbarproject.jpa.java8.extras.convert.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO: VER TEMA DE QUE SEA UNA IMPLEMENTACION DE NOTIFICABLE, LAS INTERFACES NO SE MAPPEAN


@Entity
public class Evento implements Notificable, Comparable<Evento> {
    //TODO:Contemplar  la temperatura al momento del evento.

    @Id
    @GeneratedValue
    Long id;

    private String nombre;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime fecha;
    private String lugar;

    @ManyToOne(cascade = CascadeType.ALL)
    private Usuario usuario;

    @Enumerated
    private FrecuenciaEvento frecuencia;

    //Los atuendos pueden ser muchos para muchos eventos o solo hay muchos atuendos para un unico evento?
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Atuendo> sugerenciasObtenidas = new ArrayList<>();


    public Evento() {
    }

    /**
     * Warning: crear desde el usuario
     */
    public Evento(String nombre, LocalDateTime fechaYHora, FrecuenciaEvento frecuencia, String lugar, Usuario usuario) {
        this.nombre = nombre;
        this.fecha = fechaYHora;
        this.lugar = lugar;
        this.usuario = usuario;
        this.frecuencia = frecuencia;
    }


    /**
     * Metodos
     */

    public void generarSugerencias(Meteorologo meteorologo) {
        if (Math.abs((fecha.getYear()-LocalDateTime.now().getYear())*365 + fecha.getDayOfMonth() - LocalDateTime.now().getDayOfMonth()) <= this.diasDeProximidadAEventos()) {
            sugerenciasObtenidas = usuario.obtenerSugerenciasDeTodosSusGuardarropas(meteorologo);
        } else {
            throw new TodaviaNoEstaCercaElEvento();
        }
    }

    private int diasDeProximidadAEventos() {
        return 31;
    }

    public List<Atuendo> obtenerSugerencias() {
        if (sugerenciasObtenidas.isEmpty()) {
            throw new NoHaySugerenciasParaElEvento();
        }
        return sugerenciasObtenidas;
    }

    public Atuendo buscarAtuendo(Long id){
        return sugerenciasObtenidas.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }


    public boolean seLlama(String nombre) {
        return this.nombre.equals(nombre);
    }

    /* Precision de año mes y dia */
    public boolean esEnLaFecha(LocalDateTime fecha) {
        return this.fecha.getYear() == fecha.getYear() && this.fecha.getMonthValue() == fecha.getMonthValue() && this.fecha.getDayOfMonth() == fecha.getDayOfMonth();
    }

    public boolean eshoy() {
        return esEnLaFecha(LocalDateTime.now());
    }

    public boolean estaEntre(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return fecha.isAfter(fechaInicio) && fecha.isBefore(fechaFin);
    }

    public boolean existenSugerencias() {
        return !sugerenciasObtenidas.isEmpty();
    }

    public void renovarFrecuencia() {
        frecuencia.renovarEvento(this);
    }

    public void borrarSugerenciasObtenidas(){
        sugerenciasObtenidas = new ArrayList<>();
    }


    /**
     * Observer
     */

    @Override
    public void agregarNotificado(Usuario usuario) {
        /* No hay que agregar nada por que no hay lista de notoficados, solo esta el usuario que creo el evento que quiere ser notificado */
    }

    @Override
    public void eliminarNotificado(Usuario usuario) {
        /*  No hay que agregar nada por que no hay lista de notoficados, solo esta el usuario que creo el evento que quiere ser notificado */
    }

    @Override /* Cuando un evento esta cerca se auto preparan las sugerencias y se le notifica al usuario*/
    public void notificar() {
        usuario.recibirNotificacionEventoCerca(this);
    }


    /**
     * Getters y setters
     */

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Long getId() {
        return id;
    }

    public String getLugar() {
        return lugar;
    }

    public List<Atuendo> getSugerenciasObtenidas() {
        return sugerenciasObtenidas;
    }
    /**
     * Equals y hashcode
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Evento)) return false;
        Evento evento = (Evento) o;
        return Objects.equals(nombre, evento.nombre) &&
                Objects.equals(fecha, evento.fecha) &&
                Objects.equals(lugar, evento.lugar) &&
                Objects.equals(usuario, evento.usuario) &&
                Objects.equals(sugerenciasObtenidas, evento.sugerenciasObtenidas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, fecha, lugar, usuario, sugerenciasObtenidas);
    }

    @Override
    public int compareTo(Evento o) {
        return Long.compare(this.fecha.toEpochSecond(ZoneOffset.UTC), o.fecha.toEpochSecond(ZoneOffset.UTC));
    }
}
