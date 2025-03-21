package microservice.order.microserviceorder.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * The OrderStatus enumeration.
 */
@Getter
@AllArgsConstructor
public enum OrderStatus
{
    CREATED("CREATED"),
    DELIVERING("DELIVERING"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED"),
    DELETED("DELETED"),
    RETURNING("RETURNING"),
    RETURNED("RETURNED");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
