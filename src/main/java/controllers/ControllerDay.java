package controllers;

public class ControllerDay {
    boolean esHoy;
    int numeroDia;
    boolean hayEvento = false;
    boolean esOffset = false;

    public ControllerDay(int numeroDia, boolean eshoy){
        this.numeroDia = numeroDia;
        this.esHoy = eshoy;
    }

    public ControllerDay(){
        esOffset = true;
    }

    public int getNumeroDia() {
        return numeroDia;
    }

    public boolean isEsHoy() {
        return esHoy;
    }

    public boolean isHayEvento() {
        return hayEvento;
    }

    public void setHayEvento(boolean hayEvento) {
        this.hayEvento = hayEvento;
    }

    public boolean isEsOffset() {
        return esOffset;
    }
}
