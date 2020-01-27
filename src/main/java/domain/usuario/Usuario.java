package domain.usuario;

import clima.Clima;
import clima.Meteorologo;
import domain.atuendo.Atuendo;
import domain.decision.Aceptar;
import domain.decision.Calificar;
import domain.decision.Decision;
import domain.decision.Rechazar;
import domain.evento.Evento;
import domain.evento.FrecuenciaEvento;
import domain.guardarropas.Guardarropas;
import domain.notificaciones.InteresadoAlertaMeteorologica;
import domain.notificaciones.InteresadoEvento;
import domain.notificaciones.MedioDeNotificacion;
import exceptions.ContraseñaInvalidaException;
import exceptions.ElGuardarropasNoEsAptoException;
import exceptions.NoExisteGuardarropasException;
import exceptions.NoHayDecisionesParaDeshacer;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.uqbar.commons.model.annotations.Observable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Observable /** Necesario para poder usarse con arena */

@Entity
public class Usuario implements InteresadoEvento, InteresadoAlertaMeteorologica {


    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="usuarioId_eventos")
    private List<Evento> eventos = new ArrayList<Evento>();

    private String nombre;

    private int hashPass = 0;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name= "usuarioId_decisiones")
    private List<Decision> decisiones = new ArrayList<Decision>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuarioId_atuendosAceptados")
    private List<Atuendo> atuendosAceptados = new ArrayList<Atuendo>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuarioId_atuendosRechazados")
    private List<Atuendo> atuendosRechazados = new ArrayList<Atuendo>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuarioId_atuendosCalificados")
    private List<Atuendo> atuendosCalificados = new ArrayList<Atuendo>();

    @Enumerated(EnumType.STRING)
    private TipoDeUsuario tipoDeUsuario;


    /*TODO: VER TEMA GUARDARROPAS COMPARTIDO.
     TODO: SE PUEDE ARMAR LA IDEA A PARTIR DE LA LOGICA DEL DOMINIO?
     TODO: ES UNA CLASE EN PARALELO CON PREMIUM Y LIMITADO O HACE QUE ESTAS HEREDEN DE EL O DE UNO COMUN?
     TODO: DENTRO DE LA LISTA DE GUARDARROPAS DEL USUARIO O UNA LISTA PARALELA DE GUARDARROPAS COMPARTIDOS?
    */
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Guardarropas> guardarropas=new ArrayList<Guardarropas>();

    @OneToMany
    @JoinColumn(name = "usuarioId_mediosDeNotificacion")
    private Set<MedioDeNotificacion> mediosDeNotificacion = new HashSet<>();

    //Solo para que sea compatible con JPA
    public Usuario() {}


    /** Metodos */

    public Usuario(String nombre, List<Guardarropas> guardarropas, TipoDeUsuario tipoDeUsuario) {
        this.nombre=nombre;
        this.tipoDeUsuario=tipoDeUsuario;
        if(guardarropas !=null){
            guardarropas.stream().forEach( guardarropasAValidar -> validarTipoDeGuardarropas(guardarropasAValidar));
            this.guardarropas = guardarropas;
        }
        //RepositorioUsuarios.getInstance().agregarUsuario(this);
    }

    public void validarContraseña(String password){
        if(!(this.hashPass == password.hashCode()))
            throw new ContraseñaInvalidaException();
    }

    public Long getId() {
        return id;
    }


    public void agregarGuardarropas (Guardarropas guardarropasAgregado){
        validarTipoDeGuardarropas(guardarropasAgregado);
        this.guardarropas.add(guardarropasAgregado);
    }

    public void validarTipoDeGuardarropas(Guardarropas guardarropasAValidar){

        if((guardarropasAValidar.tipoDeUsuarioQueAcepta() != tipoDeUsuario)){
            throw new ElGuardarropasNoEsAptoException();
        }
    }
    public List<Atuendo> obtenerSugerenciasDeTodosSusGuardarropas(Meteorologo meteorologo){

        /**TODO:VER TEMA COEFICIENTE DE USUARIO EN BASE A CALIFICACION DE ATUENDOS*/
        return guardarropas.stream()
                .flatMap(guardarropas -> guardarropas.sugerirAtuendo(meteorologo, this.getCoeficienteSensacionT()).stream())
                .collect(Collectors.toList());
    }

    public List<Atuendo> obtenerSugerencias(int indexGuardarropas, Meteorologo meteorologo){
        if(indexGuardarropas<0 || guardarropas.size() >= indexGuardarropas)
            throw new NoExisteGuardarropasException();
        return guardarropas.get(indexGuardarropas).sugerirAtuendo(meteorologo, this.getCoeficienteSensacionT());
    }

    public void crearEvento(String nombre, LocalDateTime fechaYHora, FrecuenciaEvento frecuencia, String lugar){
        eventos.add(new Evento(nombre, fechaYHora, frecuencia, lugar, this));
    }

    public Evento buscarEvento(String nombre){
        return eventos.stream().filter(e -> e.getNombre().equals(nombre)).findFirst().orElse(null);
    }

    public Atuendo buscarAtuendoAceptado(Long id){
        return atuendosAceptados.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Atuendo> recibirSugerenciasEvento(String nombreEvento, LocalDateTime fechaEvento){ //Tambien podriamos haber usado index en la lista
        return eventos.stream().filter(evento -> evento.seLlama(nombreEvento))
                .filter(evento -> evento.esEnLaFecha(fechaEvento))
                .collect(Collectors.toList())
                .get(0).obtenerSugerencias();
    }

    private Decision popDecision(){
        return decisiones.size() > 0 ? decisiones.remove(decisiones.size()-1) : null;
    }

    public void deshacerUltimaDecision(){
        if(this.decisiones.isEmpty())
            throw new NoHayDecisionesParaDeshacer();
        Decision d = popDecision();
        if(d != null)
            d.deshacer();
    }

    public void aceptarSugerencia(Atuendo atuendo){
        this.decisiones.add(new Aceptar(atuendo));
        this.atuendosAceptados.add(atuendo);        //TODO: podria obtenerse a partir de filtrar las decisiones del usuario
    }

    public void calificarSugerencia(Atuendo atuendo, int calificacion){
        this.decisiones.add(new Calificar(atuendo,calificacion));
        this.atuendosCalificados.add(atuendo);
        this.atuendosAceptados.remove(atuendo);
    }

    public void rechazarSugerencia(Atuendo atuendo){
        decisiones.add(new Rechazar(atuendo));
        atuendosRechazados.add(atuendo);
    }

    public void agregarMedioNotificacion(MedioDeNotificacion medio){
        if(!mediosDeNotificacion.contains(medio))
            mediosDeNotificacion.add(medio);
    }





    /** Arena */
    @Transient
    private LocalDateTime filtroEventoInicial = LocalDateTime.now();
    @Transient
    private LocalDateTime filtroEventoFinal = LocalDateTime.now();
    @Transient
    private List<Evento> eventosFiltrados = new ArrayList<>();

    public void filtrarEventosEntreRangoDeFechas(){
        eventosFiltrados = eventos.stream().filter(evento -> evento.estaEntre(filtroEventoInicial, filtroEventoFinal)).collect(Collectors.toList());
    }

    /** Getters y setters */

    public void setFiltroEventoInicial(LocalDateTime filtroEventoInicial) {
        this.filtroEventoInicial = filtroEventoInicial;
    }

    public void setFiltroEventoFinal(LocalDateTime filtroEventoFinal) {
        this.filtroEventoFinal = filtroEventoFinal;
    }

    public LocalDateTime getFiltroEventoInicial() {
        return filtroEventoInicial;
    }

    public LocalDateTime getFiltroEventoFinal() {
        return filtroEventoFinal;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public List<Evento> getEventosFiltrados() {
        return eventosFiltrados;
    }

    public void setEventosFiltrados(List<Evento> eventosFiltrados) {
        this.eventosFiltrados = eventosFiltrados;
    }

    public Guardarropas getGuardarropas(int posicion) {
        return guardarropas.get(posicion);
    }

    public List<Guardarropas> getGuardarropas() {
        return this.guardarropas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPassword(String password){
        this.hashPass = password.hashCode();
    }

    public int getHashPass() {
        return hashPass;
    }

    public List<Atuendo> getAtuendosAceptados() {
        return atuendosAceptados;
    }

    public List<Atuendo> getAtuendosRechazados() {
        return atuendosRechazados;
    }

    public List<Atuendo> getAtuendosCalificados() {
        return atuendosCalificados;
    }

    public double getCoeficienteSensacionT(){
        final int cantidadCalificacionesPromediadas= 3;

        List<Atuendo> atuendosCalificados = this.getAtuendosCalificados();

        if(atuendosCalificados.size() == 0)
            return 0;

        return (double) atuendosCalificados.subList(Math.max(atuendosCalificados.size() - cantidadCalificacionesPromediadas, 0), atuendosCalificados.size()).
                stream().
                map(a -> a.getCalificacion()).
                reduce(0, (suma, calificacion) -> suma + calificacion) / (atuendosCalificados.size() - Math.max(atuendosCalificados.size() - cantidadCalificacionesPromediadas, 0));
    }

    /** Observer */

    @Override /* El usuario va a recibir el evento que este cerca y con sugerencias preparadas */
    public void recibirNotificacionEventoCerca(Evento evento) {
        mediosDeNotificacion.stream().forEach(medio -> medio.lanzarNotificacion());
    }

    @Override
    public void recibirNotificacionAlertaMeteorologica(Clima climaActual) {
        if(atuendosAceptados.stream().anyMatch(atuendo -> atuendo.revalidadAtuendo(climaActual)))
            mediosDeNotificacion.stream().forEach(medio -> medio.lanzarNotificacion());
    }





    /** Equals y hashCode */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(eventos, usuario.eventos) &&
                Objects.equals(decisiones, usuario.decisiones) &&
                Objects.equals(atuendosAceptados, usuario.atuendosAceptados) &&
                Objects.equals(atuendosRechazados, usuario.atuendosRechazados) &&
                Objects.equals(guardarropas, usuario.guardarropas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventos, decisiones, atuendosAceptados, atuendosRechazados, guardarropas);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
