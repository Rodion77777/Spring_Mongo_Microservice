package microservice.customer.microservicecustomer.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Objects;

@Data
@ToString
@NoArgsConstructor
public class Order implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @NotBlank
    private String id;

    @NotBlank
    private String customerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
