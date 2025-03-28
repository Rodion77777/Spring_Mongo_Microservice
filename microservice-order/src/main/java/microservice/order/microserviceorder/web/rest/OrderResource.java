package microservice.order.microserviceorder.web.rest;

import jakarta.validation.Valid;
import microservice.order.microserviceorder.domain.Order;
import microservice.order.microserviceorder.exceptions.BadRequestAlertException;
import microservice.order.microserviceorder.repository.OrderRepository;
import microservice.order.microserviceorder.service.OrderService;
import microservice.order.microserviceorder.util.HeaderUtil;
import microservice.order.microserviceorder.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class OrderResource
{
    private final Logger logger = LoggerFactory.getLogger(OrderResource.class);
    private static final String ENTITY_NAME = "order";

    @Value("${spring.application.name}")
    private String applicationName;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public OrderResource(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    /**
     * {@code POST  /orders} : Create a new order.
     *
     * @param order the order to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new order, or with status {@code 400 (Bad Request)} if the order has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/orders")
    @Transactional
    public ResponseEntity<Order> createOrder (@Valid @RequestBody Order order) throws URISyntaxException
    {
        logger.debug("REST request to save Order : {}", order);
        if (order.getId() != null)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new order cannot already have an ID");
        final var result = orderRepository.save(order);
        orderService.createOrder(result);
        return ResponseEntity
                .created(new URI("/api/v1/orders/" + result.getId().toString()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /orders} : Updates an existing order.
     *
     * @param order the order to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated order,
     * or with status {@code 400 (Bad Request)} if the order is not valid,
     * or with status {@code 500 (Internal Server Error)} if the order couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/orders")
    @Transactional
    public ResponseEntity<Order> updateOrder(@Valid @RequestBody Order order) throws URISyntaxException {
        logger.debug("REST request to update Order : {}", order);
        if (order.getId() == null)
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        final var result = orderRepository.save(order);
        orderService.updateOrder(result);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, order.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /orders} : get all the orders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orders in body.
     */
    @GetMapping("/orders")
    @Transactional
    public List<Order> getAllOrders() {
        logger.debug("REST request to get all Orders");
        return orderRepository.findAll();
    }

    /**
     * {@code GET  /orders/:id} : get the "id" order.
     *
     * @param id the id of the order to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the order, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/orders/{id}")
    @Transactional
    public ResponseEntity<Order> getOrder(@PathVariable String id) {
        logger.debug("REST request to get Order : {}", id);
        final var order = orderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(order);
    }

    /**
     * {@code DELETE  /orders/:id} : delete the "id" order.
     *
     * @param id the id of the order to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/orders/{id}")
    @Transactional
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        logger.debug("REST request to delete Order : {}", id);

        final Optional<Order> orderOptional = orderRepository.findById(id);
        orderRepository.deleteById(id);
        orderOptional.ifPresent(orderService::deleteOrder);

        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id))
                .build();
    }
}
