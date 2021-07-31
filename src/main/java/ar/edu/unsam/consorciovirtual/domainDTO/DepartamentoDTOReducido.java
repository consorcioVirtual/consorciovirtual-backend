package ar.edu.unsam.consorciovirtual.domainDTO;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import lombok.Data;

@Data
public class DepartamentoDTOReducido {

    private long id;
    private String piso;
    private String nroDepartamento;
    private String torre;

    public static DepartamentoDTOReducido fromDepartamento(Departamento departamento){
        DepartamentoDTOReducido deptoDTO = new DepartamentoDTOReducido();
        deptoDTO.setId(departamento.getId());
        deptoDTO.setNroDepartamento(departamento.getNroDepartamento());
        deptoDTO.setTorre(departamento.getTorre());
        deptoDTO.setPiso(departamento.getPiso());
        return deptoDTO;
    }
}
