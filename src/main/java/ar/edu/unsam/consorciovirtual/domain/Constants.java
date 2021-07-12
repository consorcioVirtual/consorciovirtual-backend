package ar.edu.unsam.consorciovirtual.domain;

import java.time.ZoneId;

public final class Constants {

    private Constants() {}

    public static final ZoneId ZONE_ID_ARGENTINA = ZoneId.of("America/Argentina/Buenos_Aires");

    public static final String CARPETA_DE_ARCHIVOS = "archivos/";

    public static final String CARPETA_DE_EXPENSAS = CARPETA_DE_ARCHIVOS+"expensas/";

    public static final String CARPETA_DE_RECIBOS = CARPETA_DE_ARCHIVOS+"recibosExpensas/";
}
