package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FacturaDTOParaGasto {

    private LocalDate fechaFactura;
    private String tipoFactura;
    private String puntoDeVenta;
    private String numeroFactura;
    private String cuitProveedor;
    private String cuitReceptor;
    private String cae;
    private Double importe;

    public static FacturaDTOParaGasto fromFactura(Factura factura){
        FacturaDTOParaGasto facturaDTOparaGasto = new FacturaDTOParaGasto();
        facturaDTOparaGasto.fechaFactura = factura.getFechaFactura();
        facturaDTOparaGasto.tipoFactura = factura.getTipoFactura();
        facturaDTOparaGasto.puntoDeVenta = factura.getPuntoDeVenta();
        facturaDTOparaGasto.numeroFactura = factura.getNumeroFactura();
        facturaDTOparaGasto.cuitProveedor = factura.getCuitProveedor();
        facturaDTOparaGasto.cuitReceptor = factura.getCuitReceptor();
        facturaDTOparaGasto.cae = factura.getCae();
        facturaDTOparaGasto.importe = factura.getImporte();
        return facturaDTOparaGasto;
    }
}
