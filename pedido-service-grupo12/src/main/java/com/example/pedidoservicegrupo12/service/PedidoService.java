package com.example.pedidoservicegrupo12.service;

import com.example.pedidoservicegrupo12.dto.DetallePedidoDTO;
import com.example.pedidoservicegrupo12.dto.PedidoResponseDTO;
import com.example.pedidoservicegrupo12.dto.ResumenConsolidadoClienteDTO;
import com.example.pedidoservicegrupo12.exception.RecursoNoEncontradoException;
import com.example.pedidoservicegrupo12.restclient.pago.iclient.PagoClient;
import com.example.pedidoservicegrupo12.restclient.pago.model.PagoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private static final double TOLERANCIA = 0.01;

    private final PagoClient pagoClient;


    private final Map<Long, String> clientes = Map.of(
            100L, "Ana Torres",
            200L, "Luis Ramos",
            300L, "Maria Quispe"
    );

    // Datos de Prueba
    private final Map<Long, PedidoResponseDTO> pedidos = new ConcurrentHashMap<>(Map.of(
            1L, pedido(1L, "GM-2026-0001", 100L, "PENDIENTE", LocalDateTime.of(2026, 10, 1, 9, 0),
                    detalle(10L, "Mouse inalambrico", 2, 50.0),
                    detalle(11L, "Teclado mecanico", 1, 150.0)),
            2L, pedido(2L, "GM-2026-0002", 100L, "PENDIENTE", LocalDateTime.of(2026, 10, 2, 11, 0),
                    detalle(12L, "Monitor 27 pulgadas", 1, 800.0)),
            3L, pedido(3L, "GM-2026-0003", 200L, "PENDIENTE", LocalDateTime.of(2026, 10, 3, 8, 30),
                    detalle(13L, "Audifonos bluetooth", 3, 120.0)),
            4L, pedido(4L, "GM-2026-0004", 200L, "PENDIENTE", LocalDateTime.of(2026, 10, 4, 16, 10),
                    detalle(14L, "Silla ergonomica", 1, 950.0)),
            5L, pedido(5L, "GM-2026-0005", 300L, "PAGADO", LocalDateTime.of(2026, 10, 5, 17, 0),
                    detalle(15L, "Webcam HD", 2, 75.0))
    ));

    public PedidoResponseDTO obtenerPedido(Long idPedido) {
        PedidoResponseDTO pedido = pedidos.get(idPedido);
        if (pedido == null) {
            throw new RecursoNoEncontradoException("No existe el pedido " + idPedido);
        }
        pedido.setMontoTotalCalculado(calcularMontoTotal(pedido.getListaDetalles()));
        return pedido;
    }

    public PedidoResponseDTO verificarYCompletar(Long idPedido) {
        PedidoResponseDTO pedido = obtenerPedido(idPedido);

        PagoDTO pago = pagoClient.obtenerPagoPorPedido(idPedido);

        boolean montoCoincide = Math.abs(pago.getMontoPagado() - pedido.getMontoTotalCalculado()) < TOLERANCIA;
        boolean pagoAprobado = "APROBADO".equalsIgnoreCase(pago.getEstadoPago());

        if (montoCoincide && pagoAprobado) {
            pedido.setEstadoPedido("COMPLETADO");
        }
        return pedido;
    }

    public ResumenConsolidadoClienteDTO consolidadoCliente(Long clienteId) {
        List<PedidoResponseDTO> historialPedidos = pedidos.values().stream()
                .filter(p -> p.getClienteId().equals(clienteId))
                .map(p -> obtenerPedido(p.getIdPedido()))
                .sorted((a, b) -> a.getIdPedido().compareTo(b.getIdPedido()))
                .toList();

        if (historialPedidos.isEmpty() && !clientes.containsKey(clienteId)) {
            throw new RecursoNoEncontradoException("No existe el cliente " + clienteId);
        }

        List<PagoDTO> historialPagos = pagoClient.listarPagosPorCliente(clienteId);

        double montoTotalAcumulado = redondear(historialPedidos.stream()
                .mapToDouble(PedidoResponseDTO::getMontoTotalCalculado)
                .sum());

        double totalPagadoAprobado = historialPagos.stream()
                .filter(p -> "APROBADO".equalsIgnoreCase(p.getEstadoPago()))
                .mapToDouble(PagoDTO::getMontoPagado)
                .sum();

        String estadoCuenta = (totalPagadoAprobado + TOLERANCIA >= montoTotalAcumulado) ? "AL_DIA" : "CON_DEUDAS";

        return ResumenConsolidadoClienteDTO.builder()
                .clienteId(clienteId)
                .nombreCliente(clientes.getOrDefault(clienteId, "Cliente " + clienteId))
                .totalPedidosRealizados(historialPedidos.size())
                .montoTotalAcumulado(montoTotalAcumulado)
                .historialPedidos(historialPedidos)
                .historialPagos(historialPagos)
                .estadoCuenta(estadoCuenta)
                .build();
    }

    private double calcularMontoTotal(List<DetallePedidoDTO> detalles) {
        return redondear(detalles.stream().mapToDouble(DetallePedidoDTO::getSubtotal).sum());
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private static DetallePedidoDTO detalle(Long idProducto, String nombre, int cantidad, double precio) {
        return DetallePedidoDTO.builder()
                .idProducto(idProducto)
                .nombreProducto(nombre)
                .cantidad(cantidad)
                .precioUnitario(precio)
                .subtotal(redondear(cantidad * precio))
                .build();
    }

    private static PedidoResponseDTO pedido(Long id, String codigo, Long clienteId, String estado,
                                            LocalDateTime fecha, DetallePedidoDTO... detalles) {
        return PedidoResponseDTO.builder()
                .idPedido(id)
                .codigoSeguimiento(codigo)
                .fechaEmision(fecha)
                .clienteId(clienteId)
                .estadoPedido(estado)
                .listaDetalles(new ArrayList<>(List.of(detalles)))
                .build();
    }
}
