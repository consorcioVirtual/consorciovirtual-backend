package ar.edu.unsam.consorciovirtual.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

public class ExtractorDatoDeJSON {
    public static String  extraerStringDeJson(String elJsonRecibido, String nombreDato){
        return extraeDato(elJsonRecibido, nombreDato).toString();
    }

    public static Double extraerDoubleDeJson(String elJsonRecibido, String nombreDato){
        return extraeDato(elJsonRecibido, nombreDato).getAsDouble();
    }

    public static Boolean extraerBooleanDeJson(String elJsonRecibido, String nombreDato){
        return extraeDato(elJsonRecibido, nombreDato).getAsBoolean();
    }

    public static Long extraerLongDeJson(String elJsonRecibido, String nombreDato){
        return extraeDato(elJsonRecibido, nombreDato).getAsLong();
    }

    private static JsonElement extraeDato(String elJsonRecibido, String nombreDato){
        JsonElement jsonElement = new JsonParser().parse(elJsonRecibido);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement dato = jsonObject.get(nombreDato);
        return dato;
    }
}
