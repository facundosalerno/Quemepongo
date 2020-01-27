package cron;

import clima.AccuWeather;
import domain.usuario.Usuario;
import exceptions.TodaviaNoEstaCercaElEvento;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.TimerTask;

public class VerificarEventosCercanos implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Usuario> usuarios = RepositorioUsuarios.getInstance().getListaDeUsuarios();
        usuarios.stream()
                .map(usuario -> usuario.getEventos())
                .forEach(listaEventosDeUsuarios -> listaEventosDeUsuarios.stream().forEach(evento -> {
                    try{
                        AccuWeather meteorologo = new AccuWeather();//TODO como variar los meteorologos
                        evento.generarSugerencias(meteorologo);
                        evento.notificar();
                    }catch (TodaviaNoEstaCercaElEvento e){
                        /* salteo y no notifico */
                    }
                }));
    }
}
