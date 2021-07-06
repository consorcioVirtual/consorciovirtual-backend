package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

@Data
public class DepartamentoDTOParaListado {
    private long id;
    private String propietario;
    private String inquilino;
    private String actividad;
    private String estadoDeCuenta;
    private String piso;
    private String nroDepartamento;



    public static DepartamentoDTOParaListado fromDepartamento(Departamento departamento){
        DepartamentoDTOParaListado unDepartamentoDto = new DepartamentoDTOParaListado();
        unDepartamentoDto.id = departamento.getId();
        unDepartamentoDto.propietario = departamento.getNombrePropietario();
        unDepartamentoDto.inquilino = departamento.getNombreInquilino();
        unDepartamentoDto.actividad = "Falta";
        unDepartamentoDto.estadoDeCuenta = departamento.getEstadoDeCuenta();
        unDepartamentoDto.piso = departamento.getPiso();
        unDepartamentoDto.nroDepartamento = departamento.getNroDepartamento();
        return unDepartamentoDto;
    }

    public static DepartamentoDTOParaListado fromDepartamentoByUser(Departamento departamento){
        DepartamentoDTOParaListado unDepartamentoDto = new DepartamentoDTOParaListado();
        unDepartamentoDto.id = departamento.getId();
        unDepartamentoDto.piso = departamento.getPiso();
        unDepartamentoDto.nroDepartamento = departamento.getNroDepartamento();
        return unDepartamentoDto;
    }

}
