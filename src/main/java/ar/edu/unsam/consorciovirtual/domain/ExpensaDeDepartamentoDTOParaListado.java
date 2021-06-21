package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

import java.time.YearMonth;

@Data
public class ExpensaDeDepartamentoDTOParaListado {
    private Long id;
    private YearMonth periodo;
    private String departamento;
    private Double montoAPagar;
    private String estado;

    public static ExpensaDeDepartamentoDTOParaListado fromExpensaDeDepartamento(ExpensaDeDepartamento expensa){
        ExpensaDeDepartamentoDTOParaListado expensaDto = new ExpensaDeDepartamentoDTOParaListado();
        expensaDto.id = expensa.getId();
        expensaDto.periodo = expensa.getPeriodo();
        expensaDto.departamento = expensa.getUnidad();
        expensaDto.montoAPagar = expensa.getMontoAPagar();
        expensaDto.estado = expensa.getEstado();
        return expensaDto;
    }

}
