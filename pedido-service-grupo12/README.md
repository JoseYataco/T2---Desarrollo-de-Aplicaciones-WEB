# pedido-service-grupo12

Microservicio de pedidos - GlobalMarket
(Spring Boot 4.1.1, Spring Cloud 2025.1.3, Java 25)

## Datos generales
- Puerto: 8071
- Registrado en Eureka como PEDIDO-SERVICE-GRUPO12
- Cliente del Config Server (perfil dev): https://github.com/LeitoDLight/config-server-grupoA
- Archivo que consume: pedido-service-dev.properties de https://github.com/LeitoDLight/config-repo-grupoA

## Pregunta 2.3 - Consumo del Config Server y recarga dinámica
- pom.xml: se agregaron spring-cloud-starter-config y spring-boot-starter-actuator.
- application.properties:

        spring.profiles.active=dev
        spring.config.import=configserver:http://localhost:8888
        spring.cloud.config.name=pedido-service
        management.endpoints.web.exposure.include=refresh,health,info
        eureka.client.refresh.enable=false

- ConfiguracionController: anotado con @RefreshScope, inyecta con @Value las propiedades ecom.* y spring.datasource.url.
- Endpoint: GET /api/v1/pedidos/configuracion

        {
          "impuesto-igv": 0.18,
          "monto-minimo-envio-gratis": 150.00,
          "mensaje-bienvenida": "Entorno de DESARROLLO - Pedidos Activos Grupo A",
          "datasource.url": "jdbc:postgresql://localhost:5432/ecommerce_dev_db"
        }

`spring.cloud.config.name=pedido-service` es necesario porque el nombre de la aplicación lleva el sufijo -grupo12, pero los archivos se llaman pedido-service-{perfil}.properties.

`eureka.client.refresh.enable=false` evita que el refresh recree el cliente Eureka; sin esa línea, POST /actuator/refresh responde 500 con Spring Boot 4.1.1 y Spring Cloud 2025.1.3.

## Pregunta 2.4 - Prueba de recarga en caliente
Orden de arranque: config-server-grupoA (8888), discovery-server-grupo12 (8761), pedido-service-grupo12 (8071), pago-service-grupo12 (8072), api-gateway-grupo12 (8070).

1. GET http://localhost:8070/api/v1/pedidos/configuracion -> monto-minimo-envio-gratis = 150.00
2. Editar en GitHub pedido-service-dev.properties: ecom.monto-minimo-envio-gratis=250.00 (commit)
3. POST http://localhost:8071/actuator/refresh -> ["config.client.version","ecom.monto-minimo-envio-gratis"]
4. GET http://localhost:8070/api/v1/pedidos/configuracion -> monto-minimo-envio-gratis = 250.00 (sin reiniciar pedido-service)

## Integrantes - Pregunta 2
| Integrante | Código | Responsabilidad |
|---|---|---|
| Leonardo Dorregaray Guevara | I201924621 | 2.2 Config Server (config-server-grupoA y config-repo-grupoA) 
| Alberto Muzaurieta Hidalgo | I201623265 | 2.3 Consumo en pedido-service y 2.4 Recarga dinámica 
