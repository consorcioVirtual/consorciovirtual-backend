package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

@Data
public class DepartamentoConUsuarios {

    private Departamento departamento;
    private Long propietarioId;
    private Long inquilinoId;

}
