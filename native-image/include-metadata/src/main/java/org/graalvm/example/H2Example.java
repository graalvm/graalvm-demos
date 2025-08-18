/*
 * Copyright (c) 2024, 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package org.graalvm.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class H2Example {

    public static final String JDBC_CONNECTION_URL = "jdbc:h2:./data/test";

    public static void main(String[] args) throws Exception {
        withConnection(JDBC_CONNECTION_URL, connection -> {
            connection.prepareStatement("DROP TABLE IF EXISTS customers").execute();
            connection.commit();
        });

        Set<String> customers = Set.of("Lord Archimonde", "Arthur", "Gilbert", "Grug");

        System.out.println("=== Inserting the following customers in the database: ");
        printCustomers(customers);

        withConnection(JDBC_CONNECTION_URL, connection -> {
            connection.prepareStatement("CREATE TABLE customers(id INTEGER AUTO_INCREMENT, name VARCHAR)").execute();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO customers(name) VALUES (?)");
            for (String customer : customers) {
                statement.setString(1, customer);
                statement.executeUpdate();
            }
            connection.commit();
        });

        System.out.println("");
        System.out.println("=== Reading customers from the database.");
        System.out.println("");

        Set<String> savedCustomers = new HashSet<>();
        withConnection(JDBC_CONNECTION_URL, connection -> {
            try (ResultSet resultSet = connection.prepareStatement("SELECT * FROM customers").executeQuery()) {
                while (resultSet.next()) {
                    savedCustomers.add(resultSet.getObject(2, String.class));
                }
            }
        });

        System.out.println("=== Customers in the database: ");
        printCustomers(savedCustomers);
    }

    private static void printCustomers(Set<String> customers) {
        List<String> customerList = new ArrayList<>(customers);
        customerList.sort(Comparator.naturalOrder());
        int i = 0;
        for (String customer : customerList) {
            System.out.println((i + 1) + ". " + customer);
            i++;
        }
    }

    private static void withConnection(String url, ConnectionCallback callback) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url)) {
            connection.setAutoCommit(false);
            callback.run(connection);
        }
    }

    private interface ConnectionCallback {
        void run(Connection connection) throws SQLException;
    }
}
