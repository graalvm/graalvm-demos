/*
 * Copyright (c) 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
