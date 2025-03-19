package microservice.order.microserviceorder.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serial;
import java.io.Serializable;

/**
 * Product.
 */
@Data
@ToString
@NoArgsConstructor
public class Product implements Serializable
{
    @Serial
    private static final long serialVersionUID = 2L;

    @Id
    @NotNull
    private String id;

    @NotNull
    private String name;

    private String description;
    private String modelNumber;

    @NotNull
    private String manufacturerName;

    @NotNull
    @Min(value = 0, message = "Price cannot be less than zero")
    private Double price;

    private String detailInfo;
    private String imageUrl;

    @NotNull
    @Min(value = 0, message = "Quantity cannot be less than zero")
    private Integer quantity;
}
