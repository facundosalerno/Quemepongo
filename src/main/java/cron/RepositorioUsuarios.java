package cron;

import domain.usuario.TipoDeUsuario;
import domain.usuario.Usuario;
import exceptions.UsuarioInexistente;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RepositorioUsuarios implements WithGlobalEntityManager {
    static private RepositorioUsuarios instancia = null;
    private List<Usuario> listaDeUsuarios = new ArrayList<>();

    private RepositorioUsuarios(){

    }

    public static RepositorioUsuarios getInstance(){
        if(instancia == null){
            return instancia = new RepositorioUsuarios();
        }
        return instancia;
    }


    public void agregarUsuario(Usuario usuario){
        /*
        if(!listaDeUsuarios.contains(usuario))
            listaDeUsuarios.add(usuario);
        */

        if(usuario.getId() == null) {  //TODO: verificar que ande y que sea correcto
            entityManager().persist(usuario);
        }
    }


    public void eliminarUsuario(Usuario usuario){
        if(listaDeUsuarios.contains(usuario))
            listaDeUsuarios.remove(usuario);
    }

    public Usuario buscarUsuario(String nombre){
        Usuario usuario =  entityManager().createQuery("from Usuario where nombre = '"+nombre+"'", Usuario.class).getResultList().stream().findFirst().orElse(null);
        //find(Usuario.class, nombre);//listaDeUsuarios.stream().filter(u -> u.getNombre().equals(nombre)).findFirst().orElse(null);
        if(usuario == null)
            throw new UsuarioInexistente();
        return usuario;
    }

    public List<Usuario> getListaDeUsuarios(){
        return listaDeUsuarios;
    }

    public static Usuario admin = new Usuario("admin", Arrays.asList(RepositorioGuardarropas.guardarropasDelAdmin, RepositorioGuardarropas.guardarropasDelAdminAuxiliar), TipoDeUsuario.PREMIUM);
    public static LocalDateTime fechaCumpleWilly = LocalDateTime.of(2019,10,4,20,30);
    public static LocalDateTime fechaCumplePepe = LocalDateTime.of(2019,10,19,20,30);
    public static LocalDateTime fechaCumpleRoberto = LocalDateTime.of(2019,10,15,20,30);
    public static LocalDateTime entregaDiseño = LocalDateTime.of(2019,10,25,20,30);
}
