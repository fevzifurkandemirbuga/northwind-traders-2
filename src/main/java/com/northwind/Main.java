package com.northwind;

import org.apache.commons.dbcp2.BasicDataSource;

public class Main {

    public static void main(String[] args) {
        String url="jdbc:mysql://localhost:3306/northwind";
        String username=args[0];
        String password=args[1];

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);





    }

}
