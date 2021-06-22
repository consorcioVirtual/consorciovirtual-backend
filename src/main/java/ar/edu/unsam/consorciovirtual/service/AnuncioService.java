package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.Anuncio;
import ar.edu.unsam.consorciovirtual.repository.AnuncioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class AnuncioService {

    private final AnuncioRepository anuncioRepository;

    public List<Anuncio> buscarTodos(){
        return anuncioRepository.findAll();
    }

    public List<Anuncio> buscarTodosLosVigentes() {
        final LocalDate fechaDeHoy = LocalDate.now();
        return anuncioRepository.buscarPorFechaDeVencimientoPosterior(fechaDeHoy);
    }

    public void registrarTodos(List<Anuncio> anuncios) {
        anuncioRepository.saveAll(anuncios);
    }
}
