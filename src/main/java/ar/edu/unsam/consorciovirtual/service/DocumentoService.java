package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.repository.DocumentoRepository;
import ar.edu.unsam.consorciovirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class DocumentoService {
    private final DocumentoRepository documentoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<DocumentoDTOParaListado> mapearADTO(List<? extends Documento> documentos){
        return documentos.stream().map(documento-> DocumentoDTOParaListado.fromDocumento(documento)).collect(Collectors.toList());
    }

    public List<DocumentoDTOParaListado> buscarTodos(String palabraBuscada){
        List<? extends Documento> documentos = documentoRepository.findByBajaLogicaFalse(palabraBuscada);
        return mapearADTO(documentos);
    }

    public Documento buscarPorId(Long id){
        return documentoRepository.findByIdAndBajaLogicaFalse(id).orElseThrow(() -> new RuntimeException("Documento no encontrado"));
    }

    public void dercargarDocumento(Long id, HttpServletResponse response) {
        Documento documento = this.buscarPorId(id);
        try{
            FileInputStream archivo = new FileInputStream(documento.getEnlaceDeDescarga());
            int longitud = archivo.available();
            byte[] datos = new byte[longitud];
            archivo.read(datos);
            archivo.close();
            response.setHeader("Content-Disposition","attachment;filename="+documento.getTitulo());
            response.setContentType("pdf");
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(datos);
            ouputStream.flush();
            ouputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void registrarTodos(List<Factura> facturas) {
        documentoRepository.saveAll(facturas);
    }

    public void createDocumento(Long idAutor, Documento nuevoDocumento) {
        Usuario autor = usuarioRepository.buscarAdministradorPorId(idAutor).orElseThrow(() -> new IllegalArgumentException ("No tiene permiso para crear documentos"));
        if(nuevoDocumento.esValido()){
            nuevoDocumento.setAutor(autor);
            documentoRepository.save(nuevoDocumento);
        }else throw new IllegalArgumentException("Los datos ingresados no son válidos");
    }

    public void modificarDocumento(Long idUsuario, Documento documentoActualizado) {
        Documento documentoViejo = documentoRepository.findById(documentoActualizado.getId()).orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        if(documentoViejo.getAutor().getId() == idUsuario){
            documentoViejo.setDescripcion(documentoActualizado.getDescripcion());
            documentoViejo.setTitulo(documentoActualizado.getTitulo());
            documentoViejo.setEnlaceDeDescarga(documentoActualizado.getEnlaceDeDescarga());
            documentoViejo.setFechaModificacion(LocalDate.now());
        } else throw new IllegalArgumentException("No puede modificar un documento que usted no creo");

        documentoRepository.save(documentoViejo);
    }

    public void setBajaLogicaDocumento(Long idDocumento, Long idUsuario) {
        Documento documento = documentoRepository.findById(idDocumento).orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        if(documento.getAutor().getId() == idUsuario){
            documento.setBajaLogica(true);
            documento.setFechaModificacion(LocalDate.now());
        } else throw new IllegalArgumentException("No puede eliminar un documento que usted no creo");

        documentoRepository.save(documento);
    }
}
