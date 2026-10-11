package com.example.pagoservicegrupo12.controller;

import com.example.pagoservicegrupo12.dto.PagoDTO;
import com.example.pagoservicegrupo12.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<PagoDTO> obtenerPorPedido(@PathVariable("idPedido") Long idPedido) {
        return ResponseEntity.ok(pagoService.obtenerPorPedido(idPedido));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PagoDTO>> listarPorCliente(@PathVariable("clienteId") Long clienteId) {
        return ResponseEntity.ok(pagoService.listarPorCliente(clienteId));
    }
}
