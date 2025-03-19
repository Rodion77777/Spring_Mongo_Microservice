package microservice.order.microserviceorder.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum HealthStatus
{
    UP("UP"),
    DOWN("DOWN");

    private final String value;
}
