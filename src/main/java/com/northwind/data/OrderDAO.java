package com.northwind.data;

import com.northwind.model.Order;

import javax.sql.DataSource;
import java.sql.*;

public class OrderDAO extends BaseDAO<Order,Integer> {


    public OrderDAO(DataSource dataSource) {
        super(dataSource, "orders", "OrderID");
    }

    @Override
    protected Order ResultSetToObject(ResultSet results) throws SQLException {
        return new Order(
                results.getInt("OrderID"),
                results.getString("CustomerID"),
                results.getInt("EmployeeId"),
                results.getTimestamp("OrderDate").toLocalDateTime(),
                results.getTimestamp("RequiredDate").toLocalDateTime(),
                results.getTimestamp("ShippedDate").toLocalDateTime(),
                results.getInt("ShipVia"),
                results.getDouble("Freight"),
                results.getString("ShipName"),
                results.getString("ShipAddress"),
                results.getString("ShipCity"),
                results.getString("ShipRegion"),
                results.getString("ShipPostalCode"),
                results.getString("ShipCountry")
        );
    }

    @Override
    public void update(Order object) {

        String query= """
                    UPDATE orders
                    SET CustomerID = ?,
                        EmployeeID = ?,
                        OrderDate = ?,
                        RequiredDate = ?,
                        ShippedDate = ?,
                        ShipVia = ?,
                        Freight = ?,
                        ShipName = ?,
                        ShipAddress = ?,
                        ShipCity = ?,
                        ShipRegion = ?,
                        ShipPostalCode = ?,
                        ShipCountry = ?,
                    WHERE OrderID =
                """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ){
            statement.setString(1, object.getCustomerID());
            statement.setInt(2, object.getEmployeeID());
            statement.setTimestamp(3, Timestamp.valueOf(object.getOrderDate()));
            statement.setTimestamp(4, Timestamp.valueOf(object.getRequiredDate()));
            statement.setTimestamp(5, Timestamp.valueOf(object.getShippedDate()));
            statement.setInt(6, object.getShipVia());
            statement.setDouble(7, object.getFreight());
            statement.setString(8, object.getShipName());
            statement.setString(9, object.getShipAddress());
            statement.setString(10, object.getShipCity());
            statement.setString(11, object.getShipRegion());
            statement.setString(12, object.getShipPostalCode());
            statement.setString(13, object.getShipCountry());

            statement.setInt(14, object.getOrderID());

            int row =statement.executeUpdate();
            System.out.printf("%d updated",row);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void add(Order object) {

        String query= """
                INSERT INTO orders(
                        CustomerID,
                        EmployeeID,
                        OrderDate,
                        RequiredDate,
                        ShippedDate,
                        ShipVia,
                        Freight,
                        ShipName,
                        ShipAddress,
                        ShipCity,
                        ShipRegion,
                        ShipPostalCode,
                        ShipCountry,
                )
                VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement
                    (query,Statement.RETURN_GENERATED_KEYS)
        ){
            statement.setString(1, object.getCustomerID());
            statement.setInt(2, object.getEmployeeID());
            statement.setTimestamp(3, Timestamp.valueOf(object.getOrderDate()));
            statement.setTimestamp(4, Timestamp.valueOf(object.getRequiredDate()));
            statement.setTimestamp(5, Timestamp.valueOf(object.getShippedDate()));
            statement.setInt(6, object.getShipVia());
            statement.setDouble(7, object.getFreight());
            statement.setString(8, object.getShipName());
            statement.setString(9, object.getShipAddress());
            statement.setString(10, object.getShipCity());
            statement.setString(11, object.getShipRegion());
            statement.setString(12, object.getShipPostalCode());
            statement.setString(13, object.getShipCountry());


        }catch (SQLException e){
            throw new RuntimeException(e);
        }


    }
}
