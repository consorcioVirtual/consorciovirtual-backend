package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.Documento;
import ar.edu.unsam.consorciovirtual.domain.DocumentoDTOParaListado;
import ar.edu.unsam.consorciovirtual.repository.DocumentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class DocumentoService {
    private final DocumentoRepository documentoRepository;

    public List<DocumentoDTOParaListado> mapearADTO(List<Documento> anuncios){
        return anuncios.stream().map(documento-> DocumentoDTOParaListado.fromDocumento(documento)).collect(Collectors.toList());
    }

    public List<DocumentoDTOParaListado> buscarTodos(String palabraBuscada){
        List<Documento> anuncios = documentoRepository.findByBajaLogicaFalse(palabraBuscada);
        return mapearADTO(anuncios);
    }

    public Documento buscarPorId(Long id){
        return documentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Documento no encontrado"));
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
}
