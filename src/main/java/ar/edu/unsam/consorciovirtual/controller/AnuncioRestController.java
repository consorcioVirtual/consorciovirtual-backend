package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.Anuncio;
import ar.edu.unsam.consorciovirtual.service.AnuncioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class AnuncioRestController {
    @Autowired
    private final AnuncioService anuncioService;

    @GetMapping("/anuncios")
    public List<Anuncio> buscarTodos() {
        return this.anuncioService.buscarTodos();
    }

    @GetMapping("/anuncios/vigentes")
    public List<Anuncio> buscarTodosLosVigentes() {
        return this.anuncioService.buscarTodosLosVigentes();
    }

}
