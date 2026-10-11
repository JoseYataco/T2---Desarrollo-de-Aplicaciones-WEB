package com.example.pedidoservicegrupo12.controller;

import com.example.pedidoservicegrupo12.dto.PedidoResponseDTO;
import com.example.pedidoservicegrupo12.dto.ResumenConsolidadoClienteDTO;
import com.example.pedidoservicegrupo12.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping("/{idPedido}")
    public ResponseEntity<PedidoResponseDTO> obtenerPedido(@PathVariable("idPedido") Long idPedido) {
        return ResponseEntity.ok(pedidoService.obtenerPedido(idPedido));
    }

    @GetMapping("/verificar-y-completar/{idPedido}")
    public ResponseEntity<PedidoResponseDTO> verificarYCompletar(@PathVariable("idPedido") Long idPedido) {
        return ResponseEntity.ok(pedidoService.verificarYCompletar(idPedido));
    }

    @GetMapping("/consolidado-cliente/{clienteId}")
    public ResponseEntity<ResumenConsolidadoClienteDTO> consolidadoCliente(@PathVariable("clienteId") Long clienteId) {
        return ResponseEntity.ok(pedidoService.consolidadoCliente(clienteId));
    }
}
