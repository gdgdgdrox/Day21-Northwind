package northwind.day21.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import northwind.day21.model.Customer;
import northwind.day21.model.Order;
import northwind.day21.repository.CustomerRepository;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepo;

    public List<Customer> findAllCustomers(Integer limit, Integer offset){
        return customerRepo.findAllCustomers(limit, offset);
    }

    public Optional<Customer> findCustomerById(Integer id){
        return customerRepo.findCustomerById(id);
    }

    public Optional<List<Order>> findCustomerOrdersByCustomerId(Integer id){
        return customerRepo.findCustomerOrdersByCustomerId(id);
    }

    public boolean customerExist(Integer id){
        return customerRepo.customerExist(id);
    }
}
