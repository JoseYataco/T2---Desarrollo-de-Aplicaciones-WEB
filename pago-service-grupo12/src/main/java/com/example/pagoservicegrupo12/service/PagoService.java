package com.example.pagoservicegrupo12.service;

import com.example.pagoservicegrupo12.dto.PagoDTO;
import com.example.pagoservicegrupo12.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoService {

    private record RegistroPago(Long clienteId, PagoDTO pago) { }

    // Datos de Prueba
    private final List<RegistroPago> registros = List.of(
            new RegistroPago(100L, PagoDTO.builder().idPago(1L).idPedido(1L).metodoPago("TARJETA_CREDITO")
                    .montoPagado(250.0).fechaPago(LocalDateTime.of(2026, 10, 1, 10, 30))
                    .codigoAutorizacion("AUT-0001").estadoPago("APROBADO").build()),
            new RegistroPago(100L, PagoDTO.builder().idPago(2L).idPedido(2L).metodoPago("TRANSFERENCIA")
                    .montoPagado(500.0).fechaPago(LocalDateTime.of(2026, 10, 2, 15, 0))
                    .codigoAutorizacion("AUT-0002").estadoPago("APROBADO").build()),
            new RegistroPago(200L, PagoDTO.builder().idPago(3L).idPedido(3L).metodoPago("TARJETA_CREDITO")
                    .montoPagado(360.0).fechaPago(LocalDateTime.of(2026, 10, 3, 9, 45))
                    .codigoAutorizacion("AUT-0003").estadoPago("RECHAZADO").build()),
            new RegistroPago(300L, PagoDTO.builder().idPago(4L).idPedido(5L).metodoPago("TRANSFERENCIA")
                    .montoPagado(150.0).fechaPago(LocalDateTime.of(2026, 10, 5, 18, 20))
                    .codigoAutorizacion("AUT-0004").estadoPago("APROBADO").build())
    );

    public PagoDTO obtenerPorPedido(Long idPedido) {
        return registros.stream()
                .map(RegistroPago::pago)
                .filter(p -> p.getIdPedido().equals(idPedido))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe pago registrado para el pedido " + idPedido));
    }

    public List<PagoDTO> listarPorCliente(Long clienteId) {
        return registros.stream()
                .filter(r -> r.clienteId().equals(clienteId))
                .map(RegistroPago::pago)
                .toList();
    }
}
