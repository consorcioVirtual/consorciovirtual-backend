package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDate;

@Data
@Entity
@PrimaryKeyJoinColumn(name="documentoId")
@JsonTypeName("factura")
public class Factura extends Documento{
    private LocalDate fechaFactura; //Es la fecha que figura en la factura electronica, distinta a la fecha de creación del decumento
    private String tipoFactura;
    private String puntoDeVenta;
    private String numeroFactura;
    private String cuitProveedor;
    private String cuitReceptor;
    private String cae;
    private Double importe;

    /*METODOS*/
    @Override
    public Boolean esValido(){

        return super.esValido() && importe > 0 && validarFecha() && validarCae() && validarNumeroFactura() &&
                validarPuntoDeVenta() && validarCuit(cuitProveedor) && validarCuit(cuitReceptor) && emisorNoEsReceptor();
    }

    private Boolean esNumero(String cadena){
        Boolean esNumerico = true;
        for (int i = 0; i < cadena.length(); i++) {
            if (!Character.isDigit(cadena.charAt(i))) {
                esNumerico  = false;
            }
        }
        return  esNumerico;
    }

    private Boolean largoDeCadena(String cadena, Integer max){
        return cadena.length() >= 1 && cadena.length() <= max;
    }

    private Boolean validarCuit(String cuit){
        return cuit.length() == 11 && esNumero(cuit);
    }

    private Boolean emisorNoEsReceptor(){return cuitReceptor != cuitProveedor;}

    private Boolean validarFecha(){
        return fechaFactura.isBefore(LocalDate.now().plusDays(1));
    }

    private Boolean validarNumeroFactura(){
        return largoDeCadena(numeroFactura, 8) && esNumero(numeroFactura);
    }

    private Boolean validarPuntoDeVenta(){
        return largoDeCadena(puntoDeVenta, 4) && esNumero(puntoDeVenta);
    }

    private Boolean validarCae(){
        return cae.length() == 14 && esNumero(cae);
    }
}
