package microservice.customer.microservicecustomer.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.customer.microservicecustomer.business.service.CustomerService;
import microservice.customer.microservicecustomer.domain.Customer;
import microservice.customer.microservicecustomer.exceptions.BadRequestAlertException;
import microservice.customer.microservicecustomer.repository.CustomerRepository;
import microservice.customer.microservicecustomer.util.HeaderUtil;
import microservice.customer.microservicecustomer.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CustomerResource
{
    private static final String ENTITY_NAME = "customer";

    @Value("${spring.application.name}")
    private String applicationName;

    @NotNull
    private final CustomerRepository customerRepository;

    /**
     * {@code POST  /customers} : Create a new customer.
     *
     * @param customer the customer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customer, or with status {@code 400 (Bad Request)} if the customer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customer);

        if (customer.getId() != null)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new customer cannot already have an ID");

        final var result = customerRepository.save(customer);
        HttpHeaders headers = HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId());
        return ResponseEntity
                .created(new URI("/api/customers/" + result.getId()))
                .headers(headers)
                .body(result);
    }

    /**
     * {@code PUT  /customers} : Updates an existing customer.
     *
     * @param customer the customer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customer,
     * or with status {@code 400 (Bad Request)} if the customer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customers")
    public ResponseEntity<Customer> updateCustomer(@Valid @RequestBody Customer customer) {
        log.debug("REST request to update Customer : {}", customer);
        if (customer.getId() == null) throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        Customer existingCustomer = customerRepository.findById(customer.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Customer savedCustomer = CustomerService.updateCustomerFrom(customer, existingCustomer);
        final var result = customerRepository.save(savedCustomer);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customer.getId()))
                .body(result);
    }

    /**
     * {@code GET  /customers} : get all the customers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customers in body.
     */
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        log.debug("REST request to get all Customers");
        return customerRepository.findAll();
    }

    /**
     * {@code GET  /customers/:id} : get the "id" customer.
     *
     * @param id the id of the customer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable String id) {
        log.debug("REST request to get Customer : {}", id);
        final var customer = customerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(customer);
    }

    /**
     * {@code DELETE  /customers/:id} : delete the "id" customer.
     *
     * @param id the id of the customer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        log.debug("REST request to delete Customer : {}", id);
        if (id == null || id.trim().isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must not be null");
        if (!customerRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        customerRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id))
                .build();
    }
}