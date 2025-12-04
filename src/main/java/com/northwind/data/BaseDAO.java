package com.northwind.data;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T, ID> {

    protected final DataSource dataSource;
    private final String tableName;
    private final String idColumnName;

    protected BaseDAO(DataSource dataSource, String tableName, String idColumnName) {
        this.dataSource = dataSource;
        this.tableName = tableName;
        this.idColumnName =idColumnName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    // ResultSet -> Entity
    protected abstract T ResultSetToObject(ResultSet results) throws SQLException;



    public List<T> findAll() {
        List<T> items = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()
        ) {
            while (results.next()) {
                items.add(ResultSetToObject(results));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    public T findById(ID id) {
        String sql = "SELECT * FROM " + getTableName() +
                " WHERE " + getIdColumnName() + " = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);

            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return ResultSetToObject(results);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void deleteById(ID id) {
        String sql = "DELETE FROM " + getTableName() +
                " WHERE " + getIdColumnName() + " = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);
            int row = statement.executeUpdate();
            System.out.printf("Rows deleted %d%n", row);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public abstract void update(T object);

    public abstract void add(T object);
}
