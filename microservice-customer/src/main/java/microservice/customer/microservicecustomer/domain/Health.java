package microservice.customer.microservicecustomer.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import microservice.order.microserviceorder.domain.HealthStatus;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Health
{
    private HealthStatus status;
}
