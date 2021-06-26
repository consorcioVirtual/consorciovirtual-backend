package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.Anuncio;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.repository.AnuncioRepository;
import ar.edu.unsam.consorciovirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class AnuncioService {

    private final AnuncioRepository anuncioRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Anuncio> buscarTodos(){
        return anuncioRepository.findByBajaLogicaFalse();
    }

    public List<Anuncio> buscarTodosLosVigentes() {
        final LocalDate fechaDeHoy = LocalDate.now();
        return anuncioRepository.buscarPorFechaDeVencimientoPosterior(fechaDeHoy);
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
        if(anuncioViejo.getAutor().getId() == idUsuario && anuncioViejo.getFechaVencimiento().isAfter(LocalDate.now())){
            anuncioViejo.setDescripcion(anuncioActualizado.getDescripcion());
            anuncioViejo.setTitulo(anuncioActualizado.getTitulo());
            anuncioViejo.setFechaVencimiento(anuncioActualizado.getFechaVencimiento());
        } else throw new IllegalArgumentException("No puede modificar un anuncio que usted no creo o que tiene una fecha de vencimiento anterior a hoy");

        anuncioRepository.save(anuncioViejo);
    }
}
