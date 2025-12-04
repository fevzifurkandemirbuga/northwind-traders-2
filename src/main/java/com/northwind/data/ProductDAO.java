package com.northwind.data;

import com.northwind.model.Product;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    DataSource dataSource;

    public ProductDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT *
                FROM products""";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()
        ) {
            while (results.next()) {
                Product product = new Product(
                        results.getInt("ProductID"),
                        results.getString("ProductName"),
                        results.getInt("SupplierID"),
                        results.getInt("CategoryID"),
                        results.getString("QuantityPerUnit"),
                        results.getDouble("UnitPrice"),
                        results.getInt("UnitsInStock"),
                        results.getInt("UnitsOnOrder"),
                        results.getInt("ReorderLevel"),
                        results.getBoolean("Discontinued")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return products;
    }

    public Product findById(int id) {

        String sql = """
                SELECT *
                FROM products
                WHERE ProductID =?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            try (ResultSet results = statement.executeQuery()) {
                if(results.next()){
                    return new Product(
                            results.getInt("ProductID"),
                            results.getString("ProductName"),
                            results.getInt("SupplierID"),
                            results.getInt("CategoryID"),
                            results.getString("QuantityPerUnit"),
                            results.getDouble("UnitPrice"),
                            results.getInt("UnitsInStock"),
                            results.getInt("UnitsOnOrder"),
                            results.getInt("ReorderLevel"),
                            results.getBoolean("Discontinued")
                    );
                }


            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return null;
    }

    public void updateProduct(Product product) {

        String query = """
                UPDATE products
                SET ProductName = ?,
                    SupplierID = ?,
                    CategoryID = ?,
                    QuantityPerUnit = ?,
                    UnitPrice = ?,
                    UnitsInStock = ?,
                    UnitsOnOrder = ?,
                    ReorderLevel = ?,
                    Discontinued = ?
                WHERE ProductID = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {

            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setInt(2, product.getSupplierID());
            preparedStatement.setInt(3, product.getCategoryID());
            preparedStatement.setString(4, product.getQuantityPerUnit());
            preparedStatement.setDouble(5, product.getUnitPrice());
            preparedStatement.setInt(6, product.getUnitsInStock());
            preparedStatement.setInt(7, product.getUnitsOnOrder());
            preparedStatement.setInt(8, product.getReorderLevel());
            preparedStatement.setBoolean(9, product.isDiscontinued());

            preparedStatement.setInt(10, product.getProductID());

            if (preparedStatement.executeUpdate()>0){
                System.out.println("Record updated");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void addProduct(Product product) {

        String query = """
                insert into products (ProductName,
                        SupplierID,
                        CategoryID,
                        QuantityPerUnit,
                        UnitPrice,
                        UnitsInStock,
                        UnitsOnOrder,
                        ReorderLevel,
                        Discontinued)
                values (?,?,?,?,?,?,?,?,?)
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setInt(2, product.getSupplierID());
            preparedStatement.setInt(3, product.getCategoryID());
            preparedStatement.setString(4, product.getQuantityPerUnit());
            preparedStatement.setDouble(5, product.getUnitPrice());
            preparedStatement.setInt(6, product.getUnitsInStock());
            preparedStatement.setInt(7, product.getUnitsOnOrder());
            preparedStatement.setInt(8, product.getReorderLevel());
            preparedStatement.setBoolean(9, product.isDiscontinued());



            if (preparedStatement.executeUpdate()>0){
                System.out.println("new record added");
            }

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    System.out.printf("new record ID: %d\n", keys.getInt(1));
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void deleteProduct(Product product){

        String query= "DELETE FROM products WHERE ProductID = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, product.getProductID());

            if (preparedStatement.executeUpdate()>0){
                System.out.println("record deleted");
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
