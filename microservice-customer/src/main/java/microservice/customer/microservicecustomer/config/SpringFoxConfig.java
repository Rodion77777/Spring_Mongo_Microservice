package microservice.customer.microservicecustomer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {
    // ➜ Создаёт и настраивает Docket — основной объект SpringFox для генерации Swagger-документации.
    @Bean
    public Docket api() {
        // ➜ Указывает, что используется Swagger 2.
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                // ➜ Включает в документацию только классы с аннотацией @RestController.
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                // ➜ Включает все эндпоинты.
                .paths(PathSelectors.any())
                .build();
    }
}