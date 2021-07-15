package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.repository.AnuncioRepository;
import ar.edu.unsam.consorciovirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
        return anuncios.stream().map(anuncio -> AnuncioDTOParaListado.fromAnuncio(anuncio)).collect(Collectors.toList());
    }

    public List<AnuncioDTOParaListado> buscarTodos(String palabraBuscada){
        List<Anuncio> anuncios = anuncioRepository.findByBajaLogicaFalseAndAutorNombreContainingOrBajaLogicaFalseAndAutorApellidoContainingOrBajaLogicaFalseAndTituloContaining(palabraBuscada, palabraBuscada, palabraBuscada);
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

    public void crearAnuncio(Long idAutor, Anuncio nuevoAnuncio) {
        validarCreacion(idAutor, nuevoAnuncio);
        Usuario autor = usuarioRepository.buscarAdministradorPorId(idAutor).orElseThrow(() -> new IllegalArgumentException ("No tiene permisos para crear anuncios"));
        nuevoAnuncio.setAutor(autor);
        anuncioRepository.save(nuevoAnuncio);
    }

    private void validarCreacion(Long idAutor, Anuncio nuevoAnuncio) {
        if(!usuarioService.usuarioEsAdminDelConsorcio(idAutor) && !usuarioService.usuarioEsAdminDeLaApp(idAutor)){
            throw new SecurityException("No tiene permisos para crear anuncios.");
        }
        if(nuevoAnuncio.getFechaVencimiento().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("La fecha de vencimiento debe ser posterior a hoy");
        }
    }

    public void modificarAnuncio(Long idLogueado, Anuncio anuncioActualizado) {
        Anuncio anuncioViejo = anuncioRepository.findById(anuncioActualizado.getId()).orElseThrow(() -> new RuntimeException("Anuncio no encontrado."));

        validarModificacion(idLogueado, anuncioViejo);
        anuncioViejo.setDescripcion(anuncioActualizado.getDescripcion());
        anuncioViejo.setTitulo(anuncioActualizado.getTitulo());
        anuncioViejo.setFechaVencimiento(anuncioActualizado.getFechaVencimiento());
        registroModificacionService.guardarPorTipoYId(TipoRegistro.ANUNCIO, anuncioViejo.getId(), usuarioService.getNombreYApellidoById(idLogueado));

        anuncioRepository.save(anuncioViejo);
    }

    private void validarModificacion(Long idLogueado, Anuncio anuncio){

        //Se asume que un anuncio puede ser modificado por CUALQUIER USUARIO ADMINISTRADOR sin importar si es o no el autor
        if( !usuarioService.usuarioEsAdminDelConsorcio(idLogueado) && !usuarioService.usuarioEsAdminDeLaApp(idLogueado)){
            throw new IllegalArgumentException("No tiene permisos para modificar el anuncio.");
        }

        if(anuncio.getFechaVencimiento().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("No puede modificar un anuncio que no está vigente.");
        }

    }

}
