package microservice.customer.microservicecustomer.repository;

import microservice.customer.microservicecustomer.domain.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer,String>
{
}
