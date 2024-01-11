package ar.com.juanferrara.ecommerceapi.config;

import ar.com.juanferrara.ecommerceapi.domain.utils.Constants;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration

@OpenAPIDefinition(
        info = @Info(
                title = "ECommerce API - Juan Ferrara",
                description = "API RESTful diseñada para la implementacion de un ecommerce satisfaciendo los requerimentos" +
                        " esenciales que conlleva disponer de uno de estos.\n" +
                        "\n**Como principales funciones se puede encontrar:** \n" +
                        "- Listado general de productos diviendolos por categorias\n" +
                        "- Posibilidad de realizar ordenes de multiples productos y pagarlas con MercadoPago\n" +
                        "- Manejo integral de las ordenes por parte de los empleados para el correcto despacho de las mismas\n" +
                        "- Gestion interna de los productos, categorias, ordenes y empleados por parte de la administracion\n \n" +
                        "El sistema posee tambien a su vez una **gestion de roles por autenticacion para distinguir el acceso a los recursos**, existen tres roles: \n" +
                        "- " + Constants.BADGED_CLIENT + "\n" +
                        "- " + Constants.BADGED_EMPLOYEE + "\n" +
                        "- " + Constants.BADGED_ADMIN  + "\n" +
                        "\nCada recurso en caso de requerir un ROL para ser accedido, en su descripción tendrá los tags de los roles con los cuales se puede acceder.\n" +
                        "\n\nSi usted desea probar complementamente el funcionamiento de esta API y requiere tener credenciales de acceso para probar los roles de administrador y empleado no dude en contactarme." +
                        "A su vez en el repositorio de este proyecto podrá encontrar videos y fotos demostrativos del funcionamiento completo\n" +
                        "\n\n**Aviso importante**: Algunas operaciones en el requestBody necesitan el userId o employeeUserId de quien este en ese momento solicitando el recurso. " +
                        "Al iniciar sesión el responseBody devuelve el userId favor de recordarlo para utilizarlo.\n" +
                        "\n\nDatos de pago para simular un pago con MercadoPago:\n" +
                        "- Numero de tarjeta: 5031 7557 3453 0604\n" +
                        "- Fecha de vencimiento: 11/25\n" +
                        "- Codigo de seguridad: 123\n",
                contact = @Contact(name = "Juan Jose Ferrara", email = "juanjoyqueco.8@hotmail.com", url = "https://www.linkedin.com/in/juan-ferrara/"),
                version = "1.0.0"),
        externalDocs = @ExternalDocumentation(description = "Repositorio del proyecto", url = "https://github.com/JuanDouCore/EcommerceApi"),
        tags = {
                @Tag(name = "Autenticacion", description = "API para la gestion de autenticaciones"),
                @Tag(name = "Vistas Categorias y Productos", description = "API para la vistas y busquedas de categorias y productos"),
                @Tag(name = "Gestor Categorias de Productos", description = "API para la gestion integral de las categorias de productos"),
                @Tag(name = "Gestor Productos", description = "API para la gestion integral de productos"),
                @Tag(name = "Ordenes", description = "API para la creacion y vista de ordenes"),
                @Tag(name = "Despacho de ordenes", description = "API para el manejo y vista del despacho de ordenes"),
                @Tag(name = "Usuarios", description = "API para la gestion integral y vista de usuarios")},
        servers = {
                @Server(url = "https://ecommerceapi-jjf.fly.dev", description = "Servidor de produccion"),
        }
)

@SecurityScheme(
        name = "Bearer Authentication",
        description = "Token JWT que permite la autorizacion al acceso a los recursos. Este puede ser solicitado al iniciar sesion desde el endpoint de autentitacion.",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenAPIConfig {
}
