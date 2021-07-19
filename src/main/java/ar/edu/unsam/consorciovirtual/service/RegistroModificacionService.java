package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.RegistroModificacion;
import ar.edu.unsam.consorciovirtual.domain.TipoRegistro;
import ar.edu.unsam.consorciovirtual.domain.TipoUsuario;
import ar.edu.unsam.consorciovirtual.repository.RegistroModificacionRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.unsam.consorciovirtual.domain.Constants.ZONE_ID_ARGENTINA;

@RequiredArgsConstructor
@Service
@Transactional
public class RegistroModificacionService {

    private final RegistroModificacionRepository registroModificacionRepository;

    public List<RegistroModificacion> buscarTodos() {
        return registroModificacionRepository.findAll();
    }

    public List<RegistroModificacion> buscarPorTipoYId(TipoRegistro tipoRegistro, Long idModificado) {
        return registroModificacionRepository.findByTipoRegistroAndIdModificadoOrderByFechaHoraModificacionAsc(tipoRegistro, idModificado);
    }

    public RegistroModificacion guardarPorTipoYId(TipoRegistro tipoRegistro, Long idModificado, String nombreUsuarioModificador) {
        RegistroModificacion registroModificacion = new RegistroModificacion();
        registroModificacion.setIdModificado(idModificado);
        registroModificacion.setTipoRegistro(tipoRegistro);
        registroModificacion.setUsuarioModificador(nombreUsuarioModificador);
        registroModificacion.setFechaHoraModificacion(LocalDateTime.now());
        return registroModificacionRepository.save(registroModificacion);
    }

    public void eliminarTodosPorTipoYId(TipoRegistro tipoRegistro, Long idModificado) {
        List<String> idsRegistrosModificacionAEliminar = buscarPorTipoYId(tipoRegistro, idModificado).stream().map(registro -> registro.getId()).collect(Collectors.toList());
        registroModificacionRepository.deleteAllById(idsRegistrosModificacionAEliminar);
    }

    public String getUltimaModificacion(TipoRegistro tipoDato, Long idDato ) {
        RegistroModificacion registro = registroModificacionRepository.findFirstByTipoRegistroAndIdModificadoOrderByFechaHoraModificacionDesc(tipoDato, idDato);

        if(registro == null){
            return "Sin modificaciones";
        } else {
            LocalDateTime fromDateTime = registro.getFechaHoraModificacion();
            LocalDateTime toDateTime = LocalDateTime.now(ZONE_ID_ARGENTINA);

            long years = ChronoUnit.YEARS.between(fromDateTime, toDateTime);
            if(years > 1) {
                return "Modificado hace " + years + " años";
            } else if(years != 0) {
                return "Modificado hace " + years + " año";
            }

            long months = ChronoUnit.MONTHS.between(fromDateTime, toDateTime);
            if(months > 1) {
                return "Modificado hace " + months + " meses";
            } else if(months != 0) {
                return "Modificado hace " + months + " mes";
            }

            long weeks = ChronoUnit.WEEKS.between(fromDateTime, toDateTime);
            if(weeks > 1) {
                return "Modificado hace " + weeks + " semanas";
            }else if(weeks != 0) {
                return "Modificado hace " + weeks + " semana";
            }

            long days = ChronoUnit.DAYS.between(fromDateTime, toDateTime);
            if(days > 1) {
                return "Modificado hace " + days + " días";
            }else if(days != 0) {
                return "Modificado hace " + days + " día";
            }

            long hours = ChronoUnit.HOURS.between(fromDateTime, toDateTime);
            if(hours > 1) {
                return "Modificado hace " + hours + " horas";
            }else if(hours != 0) {
                return "Modificado hace " + hours + " hora";
            }

            long minutes = ChronoUnit.MINUTES.between(fromDateTime, toDateTime);
            if(minutes > 1) {
                return "Modificado hace " + minutes + " minutos";
            }else if(minutes != 0) {
                return "Modificado hace " + minutes + " minuto";
            } else return "Modificado hace algunos segundos";

        }
    }
}
