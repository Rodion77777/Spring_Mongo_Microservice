package microservice.customer.microservicecustomer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class MicroserviceCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceCustomerApplication.class, args);
    }

}
