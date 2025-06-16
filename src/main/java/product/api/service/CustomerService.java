package product.api.service;

import org.springframework.stereotype.Service;
import product.api.entity.Customer;
import product.api.repository.CustomerRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer with id " + id + " not found"));
    }

    public Customer createCustomer(Customer customer) {
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer customer = getCustomerById(id);
        customer.setName(updatedCustomer.getName());
        customer.setContactInfo(updatedCustomer.getContactInfo());
        customer.setAddress(updatedCustomer.getAddress());
        customer.setUpdatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }
}

