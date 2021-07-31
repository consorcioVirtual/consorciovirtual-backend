package ar.edu.unsam.consorciovirtual.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class FormatConverter {
    private FormatConverter(){}


    public static YearMonth stringToYearMonth(String string){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        var stringDate = string.replaceAll("^\"+|\"+$", "");
        return YearMonth.parse(stringDate, formatter);
    }

    public static LocalDate stringToLocalDate(String string){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
            var stringDate = string.replaceAll("^\"+|\"+$", "");
            return LocalDate.parse(stringDate, formatter);
        }catch (Exception e){
            return null;
        }
    }

    public static Long stringToLong(String string) {
        try {
            return Long.valueOf(string);
        } catch (NumberFormatException ex){
            return null;
        }
    }

    public static Double stringToDouble(String string) {
        try {
            return Double.valueOf(string);
        } catch (NumberFormatException ex){
            return null;
        }
    }
}
