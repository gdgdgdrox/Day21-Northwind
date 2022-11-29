package northwind.day21.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Order {
    private Integer orderId;
    // private LocalDate orderDate;
    // private LocalDate shippedDate;
    private List<Product> products;
}
