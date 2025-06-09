package product.api.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI productOpenAPI() {
        Info info = new Info()
                .title("Product API")
                .version("1.0.0");
        Server localServer = new Server()
                .url("http://localhost:8080");
        return new OpenAPI()
                .info(info)
                .addServersItem(localServer);
    }
}
