package cron;

import org.mockito.cglib.core.Local;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.util.TimerTask;

public class VerificarAlertasMeteorologicas implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("scheduler test -> "+ LocalDateTime.now());
    }
}
