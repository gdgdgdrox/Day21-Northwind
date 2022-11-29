package northwind.day21.repository;

// private Integer id;
// private String firstName;
// private String lastName;
// private String address;
// private String city;
// private String countryRegion;

public class Queries {
    public static final String SQL_GET_ALL_CUSTOMERS = "select id, first_name, last_name, address, city, country_region from customers limit ? offset ?";
    public static final String SQL_GET_CUSTOMER_BY_ID = "select id, first_name, last_name, address, city, country_region from customers where id = ?";
    public static final String SQL_GET_CUSTOMER_ORDERS_BY_CUSTOMER_ID = """
            SELECT orders.id as order_id, order_date, shipped_date, product_name, category, unit_price
            FROM customers
            LEFT JOIN orders
            ON customers.id = orders.customer_id
            JOIN order_details
            ON orders.id = order_details.order_id
            JOIN products
            ON products.id = order_details.product_id
            WHERE orders.customer_id = ?
            """;
}
