package microservice.customer.microservicecustomer.business.service;

import microservice.customer.microservicecustomer.domain.Address;
import microservice.customer.microservicecustomer.domain.Customer;

public class CustomerService
{
    public static Customer updateCustomerFrom(Customer customer, Customer existingCustomer) {
        if (customer.getFirstName() != null) existingCustomer.setFirstName(customer.getFirstName());
        if (customer.getMiddleName() != null) existingCustomer.setMiddleName(customer.getMiddleName());
        if (customer.getLastName() != null) existingCustomer.setLastName(customer.getLastName());
        if (customer.getPaymentDetails() != null) existingCustomer.setPaymentDetails(customer.getPaymentDetails());
        if (customer.getBillingAddress() != null) existingCustomer.setBillingAddress(updateBillingAddressFrom(customer.getBillingAddress(), existingCustomer.getBillingAddress()));
        return existingCustomer;
    }

    private static Address updateBillingAddressFrom(Address address, Address existingAddress) {
        if (address.getId() != null) existingAddress.setId(address.getId());
        if (address.getStreetName() != null) existingAddress.setStreetName(address.getStreetName());
        if (address.getStreetNumber() != null) existingAddress.setStreetNumber(address.getStreetNumber());
        if (address.getAdditionalInfo() != null) existingAddress.setAdditionalInfo(address.getAdditionalInfo());
        if (address.getZipCode() != null) existingAddress.setZipCode(address.getZipCode());
        if (address.getCity() != null) existingAddress.setCity(address.getCity());
        if (address.getState() != null) existingAddress.setState(address.getState());
        if (address.getCountry() != null) existingAddress.setCountry(address.getCountry());
        return existingAddress;
    }
}
