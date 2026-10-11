package com.example.pedidoservicegrupo12.dto;

import com.example.pedidoservicegrupo12.restclient.pago.model.PagoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumenConsolidadoClienteDTO {
    private Long clienteId;
    private String nombreCliente;
    private Integer totalPedidosRealizados;
    private Double montoTotalAcumulado;
    private List<PedidoResponseDTO> historialPedidos;
    private List<PagoDTO> historialPagos;
    private String estadoCuenta;
}
