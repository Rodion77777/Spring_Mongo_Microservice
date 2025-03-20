package microservice.customer.microservicecustomer.web.rest;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.customer.microservicecustomer.domain.Customer;
import microservice.customer.microservicecustomer.domain.Order;
import microservice.customer.microservicecustomer.exceptions.BadRequestAlertException;
import microservice.customer.microservicecustomer.repository.CustomerRepository;
import microservice.customer.microservicecustomer.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entity linking controller
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CustomerOrderResource
{
    private static final String ENTITY_NAME = "order";

    @Value("${spring.application.name}")
    private String applicationName;
    private final CustomerRepository customerRepository;

    /**
     * {@code POST  /orders/:customerId} : Create a new order for the given "customerId" customer.
     *
     * @param customerId the id of the customer.
     * @param order      the order to create.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the new order, or with status {@code 400 (Bad Request)} if the order has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customerOrders/{customerId}")
    public ResponseEntity<Order> createOrder(@PathVariable String customerId, @Valid @RequestBody Order order) {
        log.debug("REST request to save Order : {} for Customer ID: {}", order, customerId);

        final var customer = checkCustomerByIdAndGet(customerId);
        customer.addOrder(order);
        customerRepository.save(customer);
        return ResponseEntity.ok().body(order);
    }

    /**
     * {@code PUT  /orders/:customerId} : Updates an existing order for the given "customerId" customer.
     *
     * @param customerId the id of the customer.
     * @param order      the order to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated order,
     * or with status {@code 400 (Bad Request)} if the order is not valid,
     * or with status {@code 500 (Internal Server Error)} if the order couldn't be updated.
     */
    @PutMapping("/customerOrders/{customerId}")
    public ResponseEntity<Order> updateOrder(@PathVariable String customerId, @Valid @RequestBody Order order) {
        log.debug("REST request to update Order : {} for Customer ID: {}", order, customerId);

        final var customer = checkCustomerByIdAndGet(customerId);
        final var orderSet = customer.getOrders()
                .stream()
                .map(o -> Objects.equals(o.getId(), order.getId()) ? order : o)
                .collect(Collectors.toSet());

        customer.setOrders(orderSet);
        customerRepository.save(customer);
        return ResponseEntity.ok().body(order);
    }

    /**
     * {@code GET  /orders} : get all the orders.
     *
     * @param customerId the id of the customer.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orders in body.
     */
    @GetMapping("/customerOrders/{customerId}")
    public Set<Order> getAllOrders(@PathVariable String customerId) {
        log.debug("REST request to get all Orders for customer ID: {}", customerId);
        return checkCustomerByIdAndGet(customerId).getOrders();
    }

    /**
     * {@code GET  /orders/:customerId/:orderId} : get the "orderId" order for the "customerId" customer.
     *
     * @param customerId the id of the customer.
     * @param orderId    the id of the order to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the order, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customerOrders/{customerId}/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String customerId, @PathVariable String orderId) {
        log.debug("REST request to get Order : {} for Customer ID: {}", orderId, customerId);
        final var customer = checkCustomerByIdAndGet(customerId);
        final var optionalOrder = customer.getOrders()
                .stream()
                .filter(o -> Objects.equals(o.getId(), orderId))
                .findFirst();
        return ResponseUtil.wrapOrNotFound(optionalOrder);
    }

    /**
     * {@code DELETE  /orders/:customerId/:orderId} : delete the "orderId" order for the "customerId" customer.
     *
     * @param customerId the id of the customer.
     * @param orderId    the id of the order to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customerOrders/{customerId}/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String customerId, @PathVariable String orderId) {
        log.debug("REST request to delete Order : {} for Customer ID: {}", orderId, customerId);
        final var customer = checkCustomerByIdAndGet(customerId);
        customer.getOrders().removeIf(o -> Objects.equals(o.getId(), orderId));
        customerRepository.save(customer);
        return ResponseEntity.noContent().build();
    }

    private Customer checkCustomerByIdAndGet(String customerId) {
        if (StringUtils.isBlank(customerId)) throw new BadRequestAlertException("No Customer", ENTITY_NAME, "noid");
        final Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty()) throw new BadRequestAlertException("Invalid Customer", ENTITY_NAME, "invalidcustomer");
        return customerOptional.get();
    }
}
