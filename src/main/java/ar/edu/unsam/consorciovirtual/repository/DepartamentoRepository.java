package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
//    List<Departamento> findByNroDepartamentoContainingAndBajaLogicaFalseOrNombrePropietarioContainingAndBajaLogicaFalseOrNombreInquilinoContainingAndBajaLogicaFalseOrEstadoDeCuentaContainingAndBajaLogicaFalse(String departamento, String propietario, String inquilino, String estadoDeCuenta);
    List<Departamento> findByNroDepartamentoContainingAndBajaLogicaFalseOrNombrePropietarioContainingAndBajaLogicaFalseOrNombreInquilinoContainingAndBajaLogicaFalse(String nroDepartamento, String nombrePropietario, String nombreInquilino);

}