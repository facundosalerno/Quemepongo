package cron;

import com.rabbitmq.client.ConnectionFactory;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class Cron {

    public static void main(String[] args) throws SchedulerException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        int oneDayInSeconds = 86400; //1 dia

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        JobDetail frecuenciasEventos = newJob(RenovarFrecuenciasEventos.class).build();
        Trigger triggerFrecuencias = newTrigger().startNow().withSchedule(repeatSecondlyForever(oneDayInSeconds)).build();
        scheduler.scheduleJob(frecuenciasEventos, triggerFrecuencias);

        JobDetail alertasMeteorologicas = newJob(VerificarAlertasMeteorologicas.class).build();
        Trigger triggerAlertas = newTrigger().startNow().withSchedule(repeatSecondlyForever(oneDayInSeconds)).build();
        scheduler.scheduleJob(alertasMeteorologicas, triggerAlertas);

        JobDetail eventosCercanos = newJob(VerificarEventosCercanos.class).build();
        Trigger triggerEventos = newTrigger().startNow().withSchedule(repeatSecondlyForever(oneDayInSeconds)).build();
        scheduler.scheduleJob(eventosCercanos, triggerEventos);
    }
}

