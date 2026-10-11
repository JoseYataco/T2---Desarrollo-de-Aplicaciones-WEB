package com.example.pedidoservicegrupo12.restclient.errorhandler;

import com.example.pedidoservicegrupo12.exception.RecursoNoEncontradoException;
import com.example.pedidoservicegrupo12.exception.ServicioExternoException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 404 -> new RecursoNoEncontradoException(
                    "pago-service: recurso no encontrado (" + response.request().url() + ")");
            case 500, 502, 503 -> new ServicioExternoException(
                    "pago-service no disponible o con error interno. Estado HTTP: " + response.status());
            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}
