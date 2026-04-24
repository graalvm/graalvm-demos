# Oracle JDBC Remote Wallet-Based Connection Native Image Demo

This demo shows a simple message store Java application that connects to a **remote Oracle Database** instance using the **JDBC Thin driver** and **Oracle wallet**.

> **Oracle wallet** is a secure software container that stores authentication and signing credentials for an Oracle Database. Trusted certificates are stored in the Oracle Wallet when the wallet is used for security credentials.

## Notes on Extra GraalVM Native Image Configuration

The basic Oracle JDBC driver works with a regular Oracle connection string quite easily, but Oracle Autonomous Database **wallet-based connections** require additional setup for Native Image.

This demo includes:

* extra Oracle security dependencies:
  * `oraclepki`
  * `osdt_core`
  * `osdt_cert`
* explicit registration of `OraclePKIProvider` in code
* reachability metadata for Oracle wallet keystore and certificate parsing classes (see [reachability-metadata.json](src/main/resources/META-INF/native-image/com.example/oracledb/reachability-metadata.json)).

These pieces are required because wallet-based connections depend on Oracle PKI providers, keystore implementations, resource bundles, and reflective class instantiation that must be made reachable in the native executable.

## Prerequisites

* GraalVM 25+
* An Oracle Autonomous AI Database instance

## Setup the Remote Oracle Database Connection with Wallet

[Oracle Autonomous AI Database](https://www.oracle.com/autonomous-database) uses [secure wallet-based connections](https://docs.oracle.com/en/solutions/secure-data-with-adb-proxy/configure-and-connect-oracle-autonomous-database.html) by default.

1. In the Oracle Cloud Console, navigate to your database instance, **Database Connection** -> **Download client credentials (Wallet)**, and download the wallet.

2. Unzip the wallet archive to a local directory, for example:
   ```bash
   unzip Wallet_<db-name>.zip -d ~/Wallet_<db-name>
   ```

3. Set the `TNS_ADMIN` variable to point to the wallet directory:
   ```bash
   export TNS_ADMIN="/path/to/wallet"
   ```
   The wallet directory contain files such as _tnsnames.ora_, _sqlnet.ora_, _cwallet.sso_, and other related files.

4. Open _tnsnames.ora_ and choose one of the service aliases. `<db-name>_medium` would be sufficient. Note it down.

5. Create or use a database user with permission to connect and create tables. Navigate to **Database actions** -> **SQL** and copy-paste the following SQL statement into the working window, replacing the user name and password with your values. Run it.
   ```sql
   CREATE USER appuser IDENTIFIED BY "Apppassw0rd_";
   GRANT CONNECT TO appuser;
   GRANT CREATE TABLE TO appuser;
   GRANT UNLIMITED TABLESPACE TO appuser;
   ```

6. Set the environment variables. In the terminal where you will run the application, export `JDBC_URL`:
   ```bash
   export JDBC_URL="jdbc:oracle:thin:@<db-name>_medium"
   ```
   > When you use a TNS alias from _tnsnames.ora_, it should be just the alias after `@`, not `@tcps:` before the alias.
   ```bash
   export JDBC_USER="<db-user>"
   ```
   ```bash
   export JDBC_PASSWORD="<db-password>"
   ```

   These are database user credentials, not your OCI login and not the wallet download password.

This demo uses the TNS alias form from `tnsnames.ora`, but Oracle JDBC also supports a direct TCPS wallet URL form such as:
```bash
jdbc:oracle:thin:@tcps://mydbhost:1522/mydbservice?wallet_location=/work/wallet
```

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

    The application connects to the database using `DriverManager`.
    It creates the `demo_events` table if it does not already exist, and inserts one row with a primary key in `event_id`.
    It stores the text value in the `message` field.

    This is the expected output:
      ```
      Latest rows:
      event_id                              | message                  | created_at
      --------------------------------------+--------------------------+-------------------
      f426b4ff-3774-4ab5-9cf3-c8159a333a98 | Running a native image   | 2026-04-10 22:54:38
      7446a1e2-ea0d-464a-98bb-7b5f81be7ab3 | Running a JAR            | 2026-04-10 22:51:46
      ```

Running the application as a native executable should provide faster startup time and a lower memory footprint compared to running from a JAR.

## Summary

For Native Image, wallet-based Oracle JDBC connections require additional configuration beyond the basic driver dependency.
This demo includes the Oracle PKI dependencies, security services registered in the executable, explicit provider registration, and reachability metadata needed for wallet keystore and certificate handling at runtime.
