# Oracle JDBC Connection Pooling (UCP) Native Image Demo

This demo shows a simple message store Java application that connects to a **remote Oracle Database** over a **TLS-encrypted JDBC (TCPS) connection**.
It uses **Oracle Universal Connection Pool (UCP)** instead of direct `DriverManager` calls.

> [Oracle Universal Connection Pool (UCP)](https://docs.oracle.com/en/database/oracle/oracle-database/21/jjucp/intro.html) is a Java-based connection pool designed to cache and reuse JDBC database connections.
Instead of creating a new physical JDBC connection for every operation, the application borrows a connection from the pool, uses it, and returns it.

## Notes on GraalVM Native Image Compatibility

Compared to a plain Oracle JDBC demo, this demo adds **Oracle UCP (`ucp11`)** as an extra dependency, configures `PoolDataSource`, borrows and returns connections from the pool, and prints basic pool statistics.

These changes primarily affect application structure and runtime behavior, rather than requiring significant additional Native Image configuration.

For wallet-based connections, additional Oracle dependencies are required for keystore and certificate handling:

- `oraclepki`
- `osdt_core`
- `osdt_cert`

Native Image builds also require reachability metadata for Oracle PKI (wallet, keystore, and certificate-related classes), as demonstrated in the [oracle-jdbc-wallet demo](../oracle-jdbc-wallet/).

This project includes the configuration required to support both **TLS-encrypted (TCPS)** and **wallet-based Oracle JDBC connections**, including the necessary Native Image metadata.

The setup steps below demonstrate the TCPS (non-wallet) configuration only.

## Prerequisites

* GraalVM 25+
* A remote Oracle Database instance with TCPS enabled

## Setup the Remote Oracle Database Connection

This demo was tested using a remote [Oracle Autonomous AI Database](https://www.oracle.com/autonomous-database).

1. In the Oracle Cloud Console, open your database instance, navigate to **Database Connection** -> **Connection strings**, and find the connection strings. Note these values:
   - hostname
   - port
   - service name

2. Create or use a database user with permission to connect and create tables. Navigate to **Database actions** -> **SQL** and copy-paste the following SQL statement into the working window, replacing the user name and password with your values. Run it.
   ```sql
   CREATE USER appuser IDENTIFIED BY "apppassword";
   GRANT CONNECT TO appuser;
   GRANT CREATE TABLE TO appuser;
   GRANT UNLIMITED TABLESPACE TO appuser;
   ```

3. Set the environment variables. In the terminal where you will run the application, export `JDBC_URL` in the [EZConnect format](https://docs.oracle.com/en/database/oracle/oracle-database/26/jajdb/):
   ```bash
   export JDBC_URL="jdbc:oracle:thin:@tcps://<host>:<port>/<service_name>"
   ```
   You can also use the full connection string provided by OCI (TNS URL format). **Medium** service level is sufficient.

   Export database user credentials (not your OCI login):
   ```bash
   export JDBC_USER="<db-user>"
   ```
   ```bash
   export JDBC_PASSWORD="<db-password>"
   ```

   > For this demo, do not use wallet-based settings such as `TNS_ADMIN` or `WALLET_LOCATION`.
   > If you previously used the wallet-based demo in the same shell, clear them first:
   > ```bash
   > unset TNS_ADMIN WALLET_LOCATION
   > ```

## Build and Run the Application

1. Package the application:
    ```bash
    ./mvnw package
    ```

2. Build a native executable using the [Maven plugin for GraalVM Native Image](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html):
    ```bash
    ./mvnw native:compile
    ```
    The executable `message-store` will be created under _target/_.

3. Run the application by passing some text as an argument (up to 200 characters):
    - from a JAR file:
      ```bash
      java -jar target/messages-store-1.0.0-SNAPSHOT.jar "Running a JAR"
      ```
    - from a native executable
      ```bash
      ./target/messages-store  "Running a native image"
      ```
    If you do not pass an argument, it uses a default message.

    The application connects to the database using Oracle UCP `PoolDataSource`.
    It creates the `demo_events` table if it does not already exist, and inserts one row with a primary key in `event_id`.
    It stores the text value in the `message` field.

    This is the expected output:
      ```
      Latest rows:
      event_id                              | message                  | created_at
      --------------------------------------+--------------------------+-------------------
      53df2961-0ad4-45c7-bbc2-d715fb9acb30 | Running a native image   | 2026-04-13 14:11:42
      81165dd9-4e8b-4e3f-9b0c-0ce113cfbca0 | Running a JAR            | 2026-04-13 14:04:48
      ```

Running the application as a native executable should provide faster startup time and a lower memory footprint compared to running from a JAR.

## Summary

This demo extends a basic Oracle JDBC example by introducing [Oracle Universal Connection Pool (UCP)](https://www.oracle.com/database/technologies/universal-connection-pool.html) on top of the same underlying JDBC connection.
It demonstrates how connection pooling affects application behavior, while emphasizing that Native Image configuration is driven by secure connectivity rather than pooling itself.
