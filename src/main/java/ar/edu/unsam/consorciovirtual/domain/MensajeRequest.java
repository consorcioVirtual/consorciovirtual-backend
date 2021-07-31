package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

@Data
public class MensajeRequest {
    private Long idEmisor;
    private String mensaje;

    public MensajeRequest(Long _idEmisor, String _mensaje){
        setIdEmisor(_idEmisor);
        setMensaje(_mensaje);
    }
}