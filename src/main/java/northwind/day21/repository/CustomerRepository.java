package northwind.day21.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import northwind.day21.model.Customer;
import northwind.day21.model.Order;
import northwind.day21.model.Product;

@Repository
public class CustomerRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean customerExist(Integer id){
        SqlRowSet result = jdbcTemplate.queryForRowSet(Queries.SQL_GET_CUSTOMER_BY_ID,id);
        return (result.next());
    }

    public List<Customer> findAllCustomers(Integer limit, Integer offset){
        List<Customer> customers = new ArrayList<>();
        System.out.printf("QUERYING DB FOR CUSTOMERS WITH LIMIT %d and OFFSET %d\n", limit, offset);
        SqlRowSet result = jdbcTemplate.queryForRowSet(Queries.SQL_GET_ALL_CUSTOMERS, limit, offset);
        while(result.next()){
            Customer customer = new Customer();
            customer.setId(result.getInt("id"));
            customer.setFirstName(result.getString("first_name"));
            customer.setLastName(result.getString("last_name"));
            customer.setAddress(result.getString("address"));
            customer.setCity(result.getString("city"));
            customer.setCountryRegion(result.getString("country_region"));
            customers.add(customer);
            System.out.println("ADDING CUSTOMER " + customer.getId());
        }
        return customers;
    }

    public Optional<Customer> findCustomerById(Integer id){
        SqlRowSet result = jdbcTemplate.queryForRowSet(Queries.SQL_GET_CUSTOMER_BY_ID, id);
        //if search returns a result (i.e. result is NOT empty)
        if(result.next()){
            Customer customer = new Customer();
            customer.setId(result.getInt("id"));
            customer.setFirstName(result.getString("first_name"));
            customer.setLastName(result.getString("last_name"));
            customer.setAddress(result.getString("address"));
            customer.setCity(result.getString("city"));
            customer.setCountryRegion(result.getString("country_region"));
            return Optional.of(customer);
        }
        else return Optional.empty();
    }

    public Optional<List<Order>> findCustomerOrdersByCustomerId(Integer customerId){
        SqlRowSet result = jdbcTemplate.queryForRowSet(Queries.SQL_GET_CUSTOMER_ORDERS_BY_CUSTOMER_ID,customerId);
        if (!result.next()){
            return Optional.empty();
        }
        else{
            //move cusor back to first record
            result.previous();
            
            List<Order> orders = new ArrayList<>();
            System.out.println("RETRIEVING ORDERS FOR CUSTOMER ID " + customerId);
            while(result.next()){
                Integer orderId = result.getInt("order_id");

                
                Optional<Order> order = orders.stream().filter(o -> (o.getOrderId()).equals(orderId)).findFirst();
                Product p = new Product(result.getString("product_name"), result.getString("category"), result.getDouble("unit_price"));
                if (order.isEmpty()){
                    System.out.printf("NO ORDER WITH ID %d. Creating new Order %d\n", orderId, orderId);
                    Order o = new Order();
                    o.setOrderId(orderId);
                    //keep getting exception java.time.LocalDateTime cannot be cast to class java.sql.Timestamp
                    //some of the order dates and shipped dates are null, hence need to do null check
                    // o.setOrderDate(null == result.getDate("order_date") ? null : result.getDate("order_date").toLocalDate());
                    // o.setShippedDate(null == result.getDate("shipped_date") ? null : result.getDate("shipped_date").toLocalDate());
                    List<Product> products = new ArrayList<>();
                    System.out.printf("ADDING PRODUCT %s UNDER ORDER ID %d\n",p.getProductName(), orderId);
                    products.add(p);
                    o.setProducts(products);
                    orders.add(o);
                }
                else{
                    System.out.printf("RETRIEVING ORDER %d AND ADDING PRODUCT %s\n",orderId, p.getProductName());
                    Order o = order.get();
                    o.getProducts().add(p);
                }
            }
            return Optional.of(orders);
        }
    }




}
