package com.northwind.data;

import javax.sql.DataSource;

public class ProductDAO {
    DataSource dataSource;

    public ProductDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
