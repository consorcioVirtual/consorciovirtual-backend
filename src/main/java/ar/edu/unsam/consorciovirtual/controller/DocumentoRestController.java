package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.DocumentoDTOParaABM;
import ar.edu.unsam.consorciovirtual.domain.DocumentoDTOParaListado;
import ar.edu.unsam.consorciovirtual.domain.Documento;
import ar.edu.unsam.consorciovirtual.domain.FacturaDTOParaGasto;
import ar.edu.unsam.consorciovirtual.service.DocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class DocumentoRestController {
    @Autowired
    private final DocumentoService documentoService;

    @GetMapping("/documentos")
    public List<DocumentoDTOParaListado> buscarTodos(@RequestParam(defaultValue="") String palabraBuscada) {
        return documentoService.buscarTodos(palabraBuscada);
    }

    @GetMapping("/documentos/{id}")
    public Documento buscarDocumentoPorId(@PathVariable Long id) {
        return documentoService.buscarPorId(id);
    }

    @GetMapping("/documentos/factura/{id}")
    public FacturaDTOParaGasto buscarFacturaPorId(@PathVariable Long id) {
        return documentoService.buscarFacturaPorId(id);
    }

    @GetMapping("/documentos/paraABM/{id}")
    public DocumentoDTOParaABM buscarDocumentoParaABMPorId(@PathVariable Long id) {
        return documentoService.buscarDocumentoParaABMPorId(id);
    }

    //Sirve para los documentos que están dentro del ambiente del back (como los resumenes de expensas)
    @GetMapping("/documentos/descargar/{id}")
    public void dercargarDocumento(@PathVariable() Long id, HttpServletResponse response) {
        documentoService.dercargarDocumento(id, response);
    }

    //No se le pasa el autor desde el front, se le carga en el back por idAutor
    @PostMapping("/documentos/create/{idAutor}")
    public void createDocumento(@PathVariable Long idAutor, @RequestBody Documento nuevoDocumento) {
        documentoService.createDocumento(idAutor, nuevoDocumento);
    }

    @PostMapping("/documentos/createDeGasto/{idAutor}")
    public void createDocumentoDeGasto(@PathVariable Long idAutor, @RequestBody Documento nuevoDocumento) {
        documentoService.createDocumentoDeGasto(idAutor, nuevoDocumento);
    }

    //El idUsuario se pasa para verificar que sea el mismo que lo creó.
    //Solo se puede modificar atributos de documento, los atributos de factura si están mal se debe anular y cargar de nuevo
    @PutMapping("/documentos/modificar/{idUsuario}")
    public void modificarDocumento(@PathVariable Long idUsuario, @RequestBody Documento nuevoDocumento) {
        documentoService.modificarDocumento(idUsuario, nuevoDocumento);
    }

    //El idUsuario se pasa para verificar que sea el mismo que lo creó.
    @DeleteMapping("/documentos/eliminar/{idDocumento}/{idUsuario}")
    public void eliminarDocumento(@PathVariable Long idDocumento, @PathVariable Long idUsuario) {
        documentoService.setBajaLogicaDocumento(idDocumento, idUsuario);
    }


}
