# Oracle JDBC Remote TLS-Encrypted (TCPS) Connection Native Image Demo

This demo shows a simple message store Java application that connects to a **remote Oracle Database** instance using the **JDBC Thin driver over a TLS-encrypted (TCPS) connection**.

> **TCPS** is the TLS-encrypted secure connection variant of the Oracle JDBC Thin network protocol, and is commonly used in production environments to protect credentials and application data in transit.

Compared to running an Oracle Database locally or using [Oracle Database Free container image](../oracle-jdbc-local/), this demo moves to a real remote database environment where DNS resolution matters, firewalls and network matter.

## Notes on GraalVM Native Image Compatibility

A secure connection can introduce additional considerations for [GraalVM Native Image](https://www.graalvm.org/latest/reference-manual/native-image/) compared to a plain TCP connection, especially when custom truststore, certificate, or wallet-based settings are involved.

For this demo, no special Native Image configuration was required beyond the standard Oracle JDBC setup.

## Prerequisites

* GraalVM 25+
* A remote Oracle Database instance with TCPS enabled

## Setup the Remote Oracle Database Connection

This demo was tested using a remote [Oracle Autonomous AI Database](https://www.oracle.com/autonomous-database) that accepts a **direct TCP/TLS-only JDBC connection without a wallet**.
If your database requires a wallet or `TNS_ADMIN`, use the [Wallet demo](../oracle-jdbc-wallet/) instead.

1. In the Oracle Cloud Console, open your database instance and navigate to **Database Connection** -> **Connection strings**.
   Under the **TLS-only** connection mode, choose the database service (for example, `low`, `medium`, or `high`) and obtain the JDBC connection string you want to use for `JDBC_URL`.

   You can use either:

   - a full TNS URL shown by the database:
     ```
     jdbc:oracle:thin:@(description=(address=(protocol=tcps)(port=<port>)(host=<your-adb-host>))(connect_data=(service_name=<your-service-name>))(security=(ssl_server_dn_match=yes)))
     ```

   - or an EZConnect URL constructed from the displayed host, port, and service name:
     ```
     jdbc:oracle:thin:@tcps://<host>:<port>/<service_name>
     ```

2. Check the network settings (DNS, listener, firewall and security rules).

   > For Oracle Autonomous Database TLS-only access, either a private endpoint must be configured or an Access Control List (ACL) must allow your client IP.

3. Create or use a database user with permission to connect and create tables. Navigate to **Database actions** -> **SQL** and copy-paste the following SQL statement into the working window, replacing the user name and password with your values. Run it.
   ```sql
   CREATE USER appuser IDENTIFIED BY "Apppassw0rd_";
   GRANT CONNECT TO appuser;
   GRANT CREATE TABLE TO appuser;
   GRANT UNLIMITED TABLESPACE TO appuser;
   ```

4. Set the environment variables in the terminal where you will run the application.

   - Export the JDBC connection string you selected in Step 1:
      ```bash
      export JDBC_URL="jdbc:oracle:thin:@<db_connection_string"
      ```
   - Export database user credentials (not your OCI login):
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
    The executable `messages-store` will be created under _target/_.

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

    The application connects to the database using `DriverManager`.
    It creates `demo_events` if it does not already exist, and inserts one row with a primary key in `event_id`.
    It stores the text value in the `message` field.

    This is the expected output:
      ```
      Latest rows:
      event_id                              | message                  | created_at
      --------------------------------------+--------------------------+-------------------
      bbabeb46-b5fe-4b3a-835b-b7a2e79fbefd | Running a native image   | 2026-04-10 13:01:26
      c6784090-fa2b-46bb-9b18-a57e7d192d4c | Running a JAR            | 2026-04-10 12:56:42
      ```

Running the application as a native executable should provide faster startup time and a lower memory footprint compared to running from a JAR.

## Summary

In this demo, the JDBC driver connected successfully over TCPS without any extra Native Image options.
More advanced setups, such as wallet-based connections or custom trust configuration, may still require additional security-related configuration.
