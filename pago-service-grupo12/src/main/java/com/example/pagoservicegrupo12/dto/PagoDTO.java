package com.example.pagoservicegrupo12.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {
    private Long idPago;
    private Long idPedido;
    private String metodoPago;
    private Double montoPagado;
    private LocalDateTime fechaPago;
    private String codigoAutorizacion;
    private String estadoPago;
}
