package domain.capaPrenda;

import clima.Clima;

public class CapasPorTemperatura {

    /** Modificadores */
    private static double menorTemperaturaPosible = -5;
    private static double mayorTemperaturaPosible = 40;
    private static int cantidadMaximaDeCapas = 4;

    /** Resuelve cuantas capas deberia tener un abrigo dado un clima. Devuelve un int entre 1 y cantidadMaximaDeCapas */
    public static int capasDeAbrigoParaClima(Clima climaActual){
        double clima = climaActual.getTemperature();
        /* Para temperaturas muy bajas el algoritmo no funciona muy bien */
        if(clima <= 1)
            return cantidadMaximaDeCapas;
        int total =  (int) Math.abs(mayorTemperaturaPosible - menorTemperaturaPosible);
        int Ncapas = (int) Math.ceil(clima * cantidadMaximaDeCapas / total); //ceil redondea para arriba en todos los casos: ej 2,3 => 3.0
        return cantidadMaximaDeCapas - Ncapas;
    }

    /* total = Math.abs(mayorTemperaturaPosible - menorTemperaturaPosible) es el 100% del rango de temperaturas aceptado
     * cantidadMaximaDeCapas es la cantidad de capas que podria llegara tener una capa compuesta como maximo (empezando por 1)
     * total ________ cantidadMaximaDeCapas
     * ClimaActual _______ n capas
     * Donde n capas es el numero inverso al que necesito => cantidadMaximaDeCapas - n capas es el resultado
     */

    /* Ejemplos:
     * 45 total ________ 4 capas
     * 20 grados _______ n = 1,7 => 2
     * devuelve 2
     *
     * 45 total ________ 4 capas
     * 30 grados _______ n = 2,6 => 3
     * devuelve 1
     *
     * 45 total ________ 4 capas
     * 10 grados _______ n = 0,8 => 1
     * devuelve 3
     *
     * 45 total ________ 4 capas
     * 5 grados _______ n = 0,4 => 1
     * devuelve 3
     *
     * 45 total ________ 4 capas
     * 0 grados _______ n = 0
     * devuelve 4 por que si
     */
}
