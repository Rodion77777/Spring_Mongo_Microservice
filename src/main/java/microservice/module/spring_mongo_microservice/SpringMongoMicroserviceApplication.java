package microservice.module.spring_mongo_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class SpringMongoMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMongoMicroserviceApplication.class, args);
    }

}
