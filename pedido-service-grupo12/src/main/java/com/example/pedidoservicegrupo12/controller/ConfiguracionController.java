package com.example.pedidoservicegrupo12.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@RefreshScope
@RestController
@RequestMapping("/api/v1/pedidos")
public class ConfiguracionController {

    @Value("${ecom.impuesto-igv}")
    private BigDecimal impuestoIgv;

    @Value("${ecom.monto-minimo-envio-gratis}")
    private BigDecimal montoMinimoEnvioGratis;

    @Value("${ecom.mensaje-bienvenida}")
    private String mensajeBienvenida;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @GetMapping("/configuracion")
    public ResponseEntity<Map<String, Object>> configuracion() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("impuesto-igv", impuestoIgv);
        body.put("monto-minimo-envio-gratis", montoMinimoEnvioGratis);
        body.put("mensaje-bienvenida", mensajeBienvenida);
        body.put("datasource.url", datasourceUrl);
        return ResponseEntity.ok(body);
    }
}
