package microservice.order.microserviceorder.repository;

import microservice.order.microserviceorder.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Order entity
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, String>
{
}