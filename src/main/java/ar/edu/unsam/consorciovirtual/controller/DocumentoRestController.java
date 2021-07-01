package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.DocumentoDTOParaListado;
import ar.edu.unsam.consorciovirtual.domain.Documento;
import ar.edu.unsam.consorciovirtual.service.DocumentoService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class DocumentoRestController {
    @Autowired
    private final DocumentoService documentoService;

    @GetMapping("/documentos")
    public List<DocumentoDTOParaListado> buscarTodos(@RequestParam(defaultValue="") String palabraBuscada) {
        return this.documentoService.buscarTodos(palabraBuscada);
    }

    @GetMapping("/documentos/descargar/{id}")
    public void dercargarDocumento(@PathVariable() Long id, HttpServletResponse response) {
        documentoService.dercargarDocumento(id, response);

    }

}
