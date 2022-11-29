package northwind.day21.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Customer {
    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String countryRegion;
    private List<Order> orders;
}
