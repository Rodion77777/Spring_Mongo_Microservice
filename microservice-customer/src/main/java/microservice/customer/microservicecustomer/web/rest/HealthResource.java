package microservice.customer.microservicecustomer.web.rest;

import microservice.customer.microservicecustomer.domain.Health;
import microservice.customer.microservicecustomer.domain.HealthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HealthResource
{
    private final Logger logger = LoggerFactory.getLogger(HealthResource.class);

    @GetMapping(value = "/health", produces = "application/json")
    public ResponseEntity<Health> getHealth() {
        logger.debug("REST request to get the Health Status");
        final var health = new Health();
        health.setStatus(HealthStatus.UP);
        return ResponseEntity.ok().body(health);
    }
}
