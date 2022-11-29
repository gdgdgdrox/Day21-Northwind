package northwind.day21.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import northwind.day21.model.Customer;
import northwind.day21.model.Order;
import northwind.day21.service.CustomerService;

@RestController
@RequestMapping(path = "/api/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

    @Autowired
    private CustomerService customerSvc;

        //Task 1 - Return all customers
        @GetMapping
        public ResponseEntity<String> customers2(@RequestParam(defaultValue="5") Integer limit,
                                                @RequestParam(defaultValue="0") Integer offset)
    {   
        List<Customer> customers = customerSvc.findAllCustomers(limit, offset);
        
        // Convert List<Customer> to JsonArray
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (Customer c : customers){
            JsonObject obj = Json.createObjectBuilder().add("id", c.getId())
                                        .add("firstName", c.getFirstName())
                                        .add("lastName", c.getLastName())
                                        .add("address", c.getAddress())
                                        .add("city", c.getCity())
                                        .add("countryRegion", c.getCountryRegion())
                                        .build();
            builder.add(obj);
        }
        //We are done with populating JsonArrayBuilder. Now we call build().
        JsonArray arr = builder.build();
        String response = arr.toString();
        ResponseEntity<String> resp = ResponseEntity.ok().body(response);
        return resp;

        // ALTERNATIVE APPROACH USING JACKSON OBJECTMAPPER - THIS IS SO MUCH SIMPLER
        // ObjectMapper mapper = new ObjectMapper();
        // String response ="";
        // try {
        //     response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(customers);
        // } catch (JsonProcessingException e) {
        //     e.printStackTrace();
        // }
        // ResponseEntity<String> resp = ResponseEntity.ok().body(response);
        // return resp;
    }

    // Task 2 - Get Customer by ID
    @GetMapping("{id}")
    public ResponseEntity<String> getCustomer(@PathVariable Integer id){
        Optional<Customer> customer = customerSvc.findCustomerById(id);
        if (customer.isEmpty()){
            System.out.printf("UNABLE TO FIND CUSTOMER WITH ID %d",id);
            JsonObject errorObj = Json.createObjectBuilder().add("error", "unable to locate customer id" + id).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorObj.toString());
        }
        else{
            Customer c = customer.get();
            return ResponseEntity.ok().body(c.toString());
        }
    }

    // Task 3 - Return orders made by a customer
    @GetMapping("{id}/orders")
    public ResponseEntity<String> getCustomerOrders(@PathVariable Integer id) throws JsonProcessingException{
        //first, check if customer exist. !exist -> return 404
        if (!customerSvc.customerExist(id)){
            System.out.printf("UNABLE TO FIND CUSTOMER WITH ID %d",id);
            JsonObject errorObj = Json.createObjectBuilder().add("error", "unable to locate customer id" + id).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorObj.toString());
        }
        //the customer exists. Now we get customer orders
        else{
            Optional<List<Order>> orders = customerSvc.findCustomerOrdersByCustomerId(id);
            //if customer dont have any order, return an empty array
            if (orders.isEmpty()){
                JsonArray emptyArray = Json.createArrayBuilder().build();
                return ResponseEntity.ok().body(emptyArray.toString());
            }
            else{
                List<Order> orderList = orders.get();
                ObjectMapper mapper = new ObjectMapper();
                String response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(orderList);
                return ResponseEntity.ok().body(response);
                //if we want to return a JsonArray.toString(), building the JsonArray very mafan cause need to build
                //the Order object AND the nested Product object.
                // return ResponseEntity.ok().body(orderList.toString());
            }
        }
    }

}

    // Task 1 - Returning List<Customer> directly instead of converting to String
    // @GetMapping
    // public ResponseEntity<List<Customer>> customers(@RequestParam(defaultValue="5") Integer limit,
    //                                                 @RequestParam(defaultValue="0") Integer offset)
    // {   
    //     System.out.println("LIMIT" + limit);
    //     System.out.println("OFFSET" + offset);
    //     List<Customer> customers = customerSvc.findAllCustomers(limit, offset);
    //     ResponseEntity<List<Customer>> resp = ResponseEntity.ok().body(customers);
    //     return resp;
    // }

    //NOTE THAT IF YOU TRY TO INSTANTIATE AN EMPTY JSONARRAY AND SUBSEQUENTLY ADD JSONOBJECT TO IT, IT WONT WORK
    //CAUSE YOU "FINISH" BUILDING THE ARRAY ALREADY 
    //--THIS WONT WORK--
    //JsonArray arr = Json.createArrayBuilder.build();
    //for loop {arr.add(Obj)} // throws UnsupportedOperationException: null