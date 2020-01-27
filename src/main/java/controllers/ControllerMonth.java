package controllers;

import domain.evento.Evento;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class ControllerMonth {
    private int hoy;
    private String mes;
    private List<ControllerDay> diasDelMes = new ArrayList<>();
    private List<Evento> eventosDelUsuario;


    public ControllerMonth(List<Evento> eventosDelUsuario){
        mes = LocalDateTime.now().getMonth().toString();
        hoy = LocalDateTime.now().getDayOfMonth();

        int cantidadDeDias = YearMonth.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth()).lengthOfMonth();
        for(int i=1; i<=offsetDays(); ++i){
            diasDelMes.add(new ControllerDay());
        }
        for(int i=1; i<=cantidadDeDias; ++i) {
            if (i == hoy) {
                diasDelMes.add(new ControllerDay(i, true));
            }else {
                diasDelMes.add(new ControllerDay(i, false));
            }
        }
        this.eventosDelUsuario = eventosDelUsuario;
        diasDelMes.stream().forEach(dia -> dia.setHayEvento(
                eventosDelUsuario.stream().anyMatch(evento -> evento.getFecha().getYear() == LocalDateTime.now().getYear()
                        && evento.getFecha().getMonth().toString().equals(mes)
                        && evento.getFecha().getDayOfMonth() == dia.numeroDia)
        ));
    }





    private int offsetDays(){
        DayOfWeek dia = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).getDayOfWeek();
        switch (dia){
            case MONDAY:
                return 0;
            case TUESDAY:
                return 1;
            case WEDNESDAY:
                return 2;
            case THURSDAY:
                return 3;
            case FRIDAY:
                return 4;
            case SATURDAY:
                return 5;
            case SUNDAY:
                return 6;
        }
        return 0;
    }





    public String getMes() {
        return mes;
    }





    public int getHoy() {
        return hoy;
    }





    public List<ControllerDay> getDiasDelMes() {
        return diasDelMes;
    }





    public List<Evento> getEventosDelUsuario() {
        return eventosDelUsuario;
    }
}
