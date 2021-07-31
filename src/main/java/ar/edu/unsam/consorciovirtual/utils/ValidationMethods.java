package ar.edu.unsam.consorciovirtual.utils;

import java.time.LocalDate;

import static ar.edu.unsam.consorciovirtual.utils.Constants.ZONE_ID_ARGENTINA;

public class ValidationMethods {

    private ValidationMethods() {
    }

    public static Boolean stringNullOVacio(String unString) {
        return datoNull(unString) || unString.equals("");
    }

    public static Boolean fechaAnteriorAHoyONull(LocalDate unaFecha) {
        return fechaAnteriorAHoy(unaFecha) || datoNull(unaFecha);
    }

    public static Boolean fechaAnteriorAHoy(LocalDate unaFecha) {
        return unaFecha.isBefore(LocalDate.now(ZONE_ID_ARGENTINA));
    }

    public static Boolean fechaPosteriorAHoy(LocalDate unaFecha) {
        return !fechaAnteriorAHoy(unaFecha);
    }

    public static Boolean datoNull(Object unDato) {
        return unDato == null;
    }

    public static Boolean superaLimiteCaracteres(String unString, int limite) {
        return unString.length() > limite;
    }

    public static Boolean fueraDeLimitesDeCaracteres(String unString, int limiteInferior, int limiteSuperior){
        return unString.length() < limiteInferior && unString.length() > limiteSuperior;
    }

    public static Boolean stringNullOSuperaLimite(String unString, int limite){
        return stringNullOVacio(unString) || superaLimiteCaracteres(unString, limite);
    }

    public static Boolean stringNullOFueraDeLimites(String unString, int limiteInferior, int limiteSuperior){
        return stringNullOVacio(unString) || fueraDeLimitesDeCaracteres(unString, limiteInferior, limiteSuperior);
    }

    public static Boolean numberNullOSuperiorA(int unNumero, int limite){
        return datoNull(unNumero) || numberSuperaLimite(unNumero, limite);
    }

    public static Boolean doubleNullOMenorIgualACero(double unNumero){
        return datoNull(unNumero) || unNumero <= 0;
    }

    public static Boolean numberSuperaLimite(int unNumero, int limite){
        return unNumero > limite;
    }


}

