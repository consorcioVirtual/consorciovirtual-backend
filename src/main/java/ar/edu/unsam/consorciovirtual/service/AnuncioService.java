package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.businessExceptions.DataConsistencyException;
import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.domainDTO.AnuncioDTOParaListado;
import ar.edu.unsam.consorciovirtual.repository.AnuncioRepository;
import ar.edu.unsam.consorciovirtual.repository.UsuarioRepository;
import ar.edu.unsam.consorciovirtual.utils.FormatConverter;
import ar.edu.unsam.consorciovirtual.utils.ValidationMethods;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class AnuncioService {

    private final AnuncioRepository anuncioRepository;
    private final UsuarioRepository usuarioRepository;
    private final RegistroModificacionService registroModificacionService;
    private final UsuarioService usuarioService;

    public List<AnuncioDTOParaListado> mapearADTO(List<Anuncio> anuncios){
        List<AnuncioDTOParaListado> dtos = anuncios.stream().map(AnuncioDTOParaListado::fromAnuncio).collect(Collectors.toList());
        dtos.forEach(this::agregarUltimaModificacion);
        return dtos;
    }

    public List<AnuncioDTOParaListado> buscarTodos(String palabraBuscada){
//        LocalDate fecha = FormatConverter.stringToLocalDate(palabraBuscada);
        List<Anuncio> anuncios = anuncioRepository.findBySearch(palabraBuscada);
        return mapearADTO(anuncios);
    }

    public List<AnuncioDTOParaListado> buscarTodosLosVigentes(String palabraBuscada) {
        final LocalDate fechaDeHoy = LocalDate.now();
        List<Anuncio> anuncios = anuncioRepository.buscarPorFechaDeVencimientoPosterior(fechaDeHoy, palabraBuscada);
        return mapearADTO(anuncios);
    }

    public Anuncio getAnuncioById(Long idAnuncio) {
        return anuncioRepository.findById(idAnuncio).orElseThrow(() -> new RuntimeException("Anuncio no encontrado"));
    }

    public void registrarTodos(List<Anuncio> anuncios) {
        anuncioRepository.saveAll(anuncios);
    }

    public void bajaLogica(Long idLogueado, Long id){
        validarBaja(idLogueado);
        Anuncio anuncio = anuncioRepository.findById(id).orElseThrow(() -> new RuntimeException("Anuncio no encontrado"));
        anuncio.setBajaLogica(true);
        registroModificacionService.eliminarTodosPorTipoYId(TipoRegistro.ANUNCIO, id);

        anuncioRepository.save(anuncio);
    }

    private void validarBaja(Long idLogueado) {
        if(!usuarioService.usuarioEsAdminDelConsorcio(idLogueado) && !usuarioService.usuarioEsAdminDeLaApp(idLogueado)){
            throw new SecurityException("No tiene permisos para eliminar anuncios.");
        }
    }

    public void crearAnuncio(Long idAutor, Anuncio nuevoAnuncio) throws DataConsistencyException {
        validarCreacion(idAutor, nuevoAnuncio);
        Usuario autor = usuarioRepository.buscarAdministradorPorId(idAutor).orElseThrow(() -> new IllegalArgumentException ("No tiene permisos para crear anuncios"));
        nuevoAnuncio.setAutor(autor);
        validarAnuncio(nuevoAnuncio);
        anuncioRepository.save(nuevoAnuncio);
    }

    private void validarCreacion(Long idAutor, Anuncio nuevoAnuncio) {
        if(!usuarioService.usuarioEsAdminDelConsorcio(idAutor) && !usuarioService.usuarioEsAdminDeLaApp(idAutor)){
            throw new SecurityException("No tiene permisos para crear anuncios.");
        }
    }

    public void modificarAnuncio(Long idLogueado, Anuncio anuncioActualizado) throws DataConsistencyException {
        Anuncio anuncioViejo = anuncioRepository.findById(anuncioActualizado.getId()).orElseThrow(() -> new RuntimeException("Anuncio no encontrado."));

        validarModificacion(idLogueado, anuncioViejo);
        anuncioViejo.setDescripcion(anuncioActualizado.getDescripcion());
        anuncioViejo.setTitulo(anuncioActualizado.getTitulo());
        anuncioViejo.setFechaVencimiento(anuncioActualizado.getFechaVencimiento());
        registroModificacionService.guardarPorTipoYId(TipoRegistro.ANUNCIO, anuncioViejo.getId(), usuarioService.getNombreYApellidoById(idLogueado));

        anuncioRepository.save(anuncioViejo);
    }

    private void validarModificacion(Long idLogueado, Anuncio anuncio) throws DataConsistencyException {

        //Se asume que un anuncio puede ser modificado por CUALQUIER USUARIO ADMINISTRADOR sin importar si es o no el autor
        if( !usuarioService.usuarioEsAdminDelConsorcio(idLogueado) && !usuarioService.usuarioEsAdminDeLaApp(idLogueado)){
            throw new IllegalArgumentException("No tiene permisos para modificar el anuncio.");
        }

        if(anuncio.getFechaVencimiento().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("No puede modificar un anuncio que no está vigente.");
        }

        validarAnuncio(anuncio);

    }

    private void agregarUltimaModificacion(@NotNull AnuncioDTOParaListado dto){
        String fechaUltimaModificacion = registroModificacionService.getUltimaModificacion(TipoRegistro.ANUNCIO, dto.getId());
        dto.setUltimaModificacion(fechaUltimaModificacion);
    }

    private void validarAnuncio(Anuncio anuncio) throws DataConsistencyException {
        if(
            ValidationMethods.stringNullOVacio(anuncio.getTitulo()) ||
            ValidationMethods.stringNullOVacio(anuncio.getDescripcion()) ||
            ValidationMethods.datoNull(anuncio.getFechaCreacion()) ||
            ValidationMethods.fechaAnteriorAHoyONull(anuncio.getFechaVencimiento()) ||
            ValidationMethods.datoNull(anuncio.getAutor())
        ) throw new DataConsistencyException("Ha ocurrido un error con los datos ingresados. Verificalos e intentá de nuevo.");
    }


}
