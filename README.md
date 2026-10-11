# Pregunta 1 – GlobalMarket S.A.C. (Microservicios)

Spring Boot 4.1.1 · Spring Cloud 2025.1.3 · Java 25 · Eureka · Spring Cloud Gateway · OpenFeign

| Proyecto | Puerto | Rol |
| discovery-server-grupo12 | 8761 | Eureka Server |
| api-gateway-grupo12 | 8070 | Punto de entrada único (rutas `lb://`) |
| pedido-service-grupo12 | 8071 | Pedidos + `@FeignClient` a pago-service |
| pago-service-grupo12 | 8072 | Pagos |


## Orden de arranque
1. discovery-server-grupo12 →  http://localhost:8761
2. pago-service-grupo12
3. pedido-service-grupo12
4. api-gateway-grupo12
 
  Dashboard de Eureka : API-GATEWAY-GRUPO12, PEDIDO-SERVICE-GRUPO12, PAGO-SERVICE-GRUPO12.

## Datos de prueba 
| Pedido | Cliente | Total | Pago | Resultado de verificar-y-completar |
| 1 | 100 Ana Torres | 250.00 | 250.00 APROBADO | **COMPLETADO** |
| 2 | 100 Ana Torres | 800.00 | 500.00 APROBADO | PENDIENTE (monto no coincide) |
| 3 | 200 Luis Ramos | 360.00 | 360.00 RECHAZADO | PENDIENTE (pago rechazado) |
| 4 | 200 Luis Ramos | 950.00 | — | 404 (sin pago registrado) |
| 5 | 300 Maria Quispe | 150.00 | 150.00 APROBADO | **COMPLETADO** |


## Pruebas (Puerto 8070)

GET http://localhost:8070/api/v1/pagos/pedido/1
GET http://localhost:8070/api/v1/pagos/cliente/100
GET http://localhost:8070/api/v1/pedidos/1
GET http://localhost:8070/api/v1/pedidos/verificar-y-completar/1
GET http://localhost:8070/api/v1/pedidos/verificar-y-completar/2
GET http://localhost:8070/api/v1/pedidos/consolidado-cliente/100
GET http://localhost:8070/api/v1/pedidos/consolidado-cliente/300

## Paticipacion de Alumnos
Jose Yataco
Joselin Carhuapoma 