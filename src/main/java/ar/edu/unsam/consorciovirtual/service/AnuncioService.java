package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.Anuncio;
import ar.edu.unsam.consorciovirtual.domain.AnuncioDTOParaListado;
import ar.edu.unsam.consorciovirtual.domain.TipoUsuario;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
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

    public List<AnuncioDTOParaListado> mapearADTO(List<Anuncio> anuncios){
        return anuncios.stream().map(anuncio -> AnuncioDTOParaListado.fromAnuncio(anuncio)).collect(Collectors.toList());
    }

    public List<AnuncioDTOParaListado> buscarTodos(String palabraBuscada){
        List<Anuncio> anuncios = anuncioRepository.findByBajaLogicaFalse(palabraBuscada);
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

    public void bajaLogica(Long id){
        Anuncio anuncio = anuncioRepository.findById(id).orElseThrow(() -> new RuntimeException("Anuncio no encontrado"));
        anuncio.setBajaLogica(true);

        anuncioRepository.save(anuncio);
    }

    public void crearAnuncio(Long idAutor, Anuncio nuevoAnuncio) {
        if(nuevoAnuncio.getFechaVencimiento().isAfter(LocalDate.now())){
            Usuario autor = usuarioRepository.buscarAdministradorPorId(idAutor).orElseThrow(() -> new IllegalArgumentException ("No tiene permiso para crear anuncios"));
            nuevoAnuncio.setAutor(autor);
            anuncioRepository.save(nuevoAnuncio);
        } else throw new IllegalArgumentException("La fecha de vencimiento debe ser posterior a hoy");
    }

    public void modificarAnuncio(Long idUsuario, Anuncio anuncioActualizado) {
        Anuncio anuncioViejo = anuncioRepository.findById(anuncioActualizado.getId()).orElseThrow(() -> new RuntimeException("Anuncio no encontrado"));
        TipoUsuario rolUsuarioModificador = usuarioRepository.findById(idUsuario).get().getTipo();

        if( (anuncioViejo.getAutor().getId() == idUsuario || rolUsuarioModificador == TipoUsuario.Administrador) && anuncioViejo.getFechaVencimiento().isAfter(LocalDate.now())){
            anuncioViejo.setDescripcion(anuncioActualizado.getDescripcion());
            anuncioViejo.setTitulo(anuncioActualizado.getTitulo());
            anuncioViejo.setFechaVencimiento(anuncioActualizado.getFechaVencimiento());
        } else throw new IllegalArgumentException("No puede modificar un anuncio que usted no creó o que tiene una fecha de vencimiento anterior a hoy");

        anuncioRepository.save(anuncioViejo);
    }

}
