package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

//    List<Departamento> findByNroDepartamentoContainingAndBajaLogicaFalseOrNombrePropietarioContainingAndBajaLogicaFalseOrNombreInquilinoContainingAndBajaLogicaFalseOrEstadoDeCuentaContainingAndBajaLogicaFalse(String departamento, String propietario, String inquilino, String estadoDeCuenta);
    List<Departamento> findByNroDepartamentoContainingAndBajaLogicaFalseOrNombrePropietarioContainingAndBajaLogicaFalseOrNombreInquilinoContainingAndBajaLogicaFalseOrPisoContainingAndBajaLogicaFalse(String nroDepartamento, String nombrePropietario, String nombreInquilino, String pisoDepto);

    List<Departamento> findByBajaLogicaFalse();

    @Query(value = "SELECT * FROM departamento where id_propietario = :id OR id_inquilino = :id" , nativeQuery=true)
    List <Departamento> buscarPorUsuario(Long id);

}