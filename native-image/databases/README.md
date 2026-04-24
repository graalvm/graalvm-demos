# Oracle JDBC Connectivity Demos for Native Image

This directory contains a set of Oracle JDBC demos organized by **connection type** and by the additional Native Image configuration each setup may require.
The complexity depends on how the application connects to Oracle Database.

> The **Oracle JDBC driver** connects directly to Oracle Database using Java sockets and its own implementation of the SQL*Net protocol over TCP/IP. Learn more in the [JDBC Developer's Guide](https://docs.oracle.com/en/database/oracle/oracle-database/21/jjdbc/connection-and-security.html).

## Overview

- [oracle-jdbc-local](oracle-jdbc-local)
  Baseline demo using a local Oracle Database Free instance in Docker.
  Uses a simple TCP connection without TLS, wallets, or external security dependencies.

- [oracle-jdbc-tls-encrypted](oracle-jdbc-tls-encrypted)
  The same baseline demo but running against a real database.
  Connects to a remote Oracle Autonomous Database using a **TLS-encrypted (TCPS) JDBC connection**.
  Introduces real-world networking concerns such as DNS, firewalls, and secure communication.

- [oracle-jdbc-wallet](oracle-jdbc-wallet)
  Demonstrates a cloud-native connection to a remote Oracle Autonomous Database using a **wallet-based connection**.
  Adds Oracle Net configuration, PKI dependencies, and required Native Image reachability metadata.

- [oracle-jdbc-connection-pooling](oracle-jdbc-connection-pooling)
  Extends the secure connection setup by introducing **Oracle Universal Connection Pool (UCP)**.
  Includes required dependencies and metadata needed for both TLS-encrypted and wallet-based connection flows.

## Conclusions

The configuration required to make a database application ready for Native Image AOT compilation depends on a connection type.
In the simplest case, if the driver class is explicitly referenced, and the application does not rely on dynamic class loading, **Oracle JDBC driver works with GraalVM Native Image without any additional configuration**.

Issues may appear if you:
- Use connection pools (for example, Oracle UCP)
- Introduce ORMs (for example, Hibernate)
- Add schema management tools (for example, Flyway or Liquibase)
- Load drivers dynamically