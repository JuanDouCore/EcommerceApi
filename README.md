# 💳 ECommerce-API 🛒 
## Descripción del Proyecto
API RESTful diseñada para representar el funcionamiento convencional de un tipico ecommerce buscando satisfacer todas las necesidades necesarias de uno de estos. Desde la muestra y gestion de productos junto con categorias, a la creacion de ordenes y pagos de las mismas con pasarela como MercadoPago.
A su vez cuenta con validaciones de seguridad automaticas para los pagos. Por otro lado posee una gestion interna para el eficiente despacho de las ordenes, y a su vez control de los clientes. Tambien posee una gestion operaria de empleados y de todo el sistema integral de la tienda.

**Deploy**: [https://ecommerceapi-jjf.fly.dev/swagger-ui/index.html](https://ecommerceapi-jjf.fly.dev/swagger-ui/index.html)

☝️☝️ Ese enlace lleva al sitio donde se pueden probar los endpoints de la API junto con su documentación. _Demora un poco en cargar al principio por el hosting_

## Tecnologías Utilizadas 🛠️
- Spring Boot 🌱
- Spring Data JPA 🔄
- Spring Security + JWT 🔐
- JUnit + Mockito 🧪
- PostgreSQL 🗃️
- MercadoPago SDK 💳
- AWS S3 ☁️

## Roles y Privilegios 🕵️‍♂️
- ![Cliente](https://img.shields.io/badge/Cliente-70F00C)   Vista general de productos y categorias. Busquedas y filtrados. Creacion de ordenes y pagos de las mismas.
- ![Empleado](https://img.shields.io/badge/Empleado-00FFD4)   Gestion del despacho de ordenes. Control de clientes. Altas y modificaciones de productos.
- ![Admin](https://img.shields.io/badge/Administrador-FF0000)   Administracion general de la tienda. Control de empleados. Gestion de productos, categorias.
  
## Configuración de Seguridad 🛡️
Se utiliza Spring Security con la tecnología de JWT para proteger el acceso a los recursos. Ciertos recursos requerirán poseer el acceso del token. También algunos recursos de manejan por roles es decir que segun el acceso que tenga el token podrá acceder a ese recurso.
También se utiliza la practica de Refresh-Token para permitir renovar los tokens de acceso antes de su expiración.

## Videos demostrativos 🎥
<a href="http://www.youtube.com/watch?feature=player_embedded&v=PI67FHyEGFk" target="_blank">
 <img src="http://img.youtube.com/vi/PI67FHyEGFk/mqdefault.jpg" alt="Watch the video" width="240" height="180" border="10" />
</a> (click a la imagen)


En este video se hace demostración del inicio de sesión de un cliente y a su vez el mismo crea una orden, efectua el pago con MercadoPago y verifica que el mismo haya sido aprobado y acreditado.

<a href="http://www.youtube.com/watch?feature=player_embedded&v=1iermtoYg3g" target="_blank">
 <img src="http://img.youtube.com/vi/1iermtoYg3g/mqdefault.jpg" alt="Watch the video" width="240" height="180" border="10" />
</a> (click a la imagen)


En este video se hace demostración del inicio de sesión de un empleado el cual va a realizar el despacho de ordenes que esten listas con el estado de PENDIENTE_ENVIO

## Endpoints API 🚀

![image](https://github.com/JuanDouCore/EcommerceApi/assets/22947314/9564697b-3826-4ba5-896e-3451ba50137a)

## Autor ✏️
[![Juan Ferrara](https://img.shields.io/badge/LinkedIn-JuanFerrara-blue)](https://www.linkedin.com/in/juan-ferrara/)
