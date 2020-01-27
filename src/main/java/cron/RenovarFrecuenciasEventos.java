package cron;

import domain.usuario.Usuario;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.TimerTask;

public class RenovarFrecuenciasEventos implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Usuario> usuarios = RepositorioUsuarios.getInstance().getListaDeUsuarios();
        usuarios.stream()
                .map(usuario -> usuario.getEventos())
                .forEach(listaEventosDeUsuarios -> listaEventosDeUsuarios.stream()
                        .filter(evento -> evento.eshoy())
                        .forEach(evento -> evento.renovarFrecuencia()));
    }
}
