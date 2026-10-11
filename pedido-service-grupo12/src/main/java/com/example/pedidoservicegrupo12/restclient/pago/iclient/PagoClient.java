package com.example.pedidoservicegrupo12.restclient.pago.iclient;

import com.example.pedidoservicegrupo12.restclient.config.FeignConfig;
import com.example.pedidoservicegrupo12.restclient.pago.model.PagoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "pago-service-grupo12", path = "/api/v1/pagos", configuration = FeignConfig.class)
public interface PagoClient {

    @GetMapping("/pedido/{idPedido}")
    PagoDTO obtenerPagoPorPedido(@PathVariable("idPedido") Long idPedido);

    @GetMapping("/cliente/{clienteId}")
    List<PagoDTO> listarPagosPorCliente(@PathVariable("clienteId") Long clienteId);
}
