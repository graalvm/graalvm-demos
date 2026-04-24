# Oracle JDBC Local Connection (TCP, Oracle Database Free) Native Image Demo

This is a simple message store Java application that connects to the official [Oracle Database Free](https://www.oracle.com/database/free/get-started/) container from the Oracle Container Registry.
This image provides a full-featured **local** Oracle Database environment.

Oracle Database Free uses a standard **local JDBC Thin driver connection over TCP** with a service name, without encryption or advanced networking layers. No Oracle client is needed.

This type of connection works well with [GraalVM Native Image](https://www.graalvm.org/latest/reference-manual/native-image/) without any extra configuration required.

### Prerequisites

* GraalVM 25+
* Docker installed and running

## Start the Oracle Database Free

Oracle Database Free is a good fit for local development and testing with the following capacities in Docker:
  * 2 CPU
  * 2 GB RAM
  * 12 GB user data

The container image is large (~9.6GB), and pulling it takes some time, but it is the simplest and fastest way to get a local Oracle database running.

1. Log in to Oracle Container Registry:
    ```bash
    docker login container-registry.oracle.com
    ```

    > If you do not want to log in to Oracle Container Registry, you can use the community-maintained Docker Hub image [gvenzl/oracle-free](https://hub.docker.com/r/gvenzl/oracle-free) instead.

2. Pull the Oracle Database Free image:
    ```bash
    docker pull container-registry.oracle.com/database/free:latest
    ```

3. Start the database:
    ```bash
    docker run -d \
      --name oracle-free \
      -p 1521:1521 \
      -e ORACLE_PWD=oraclepwd \
      container-registry.oracle.com/database/free:latest
    ```

4. Wait for the database to become ready:
    ```bash
    docker logs -f oracle-free
    ```

    When startup completes, stop following the logs with `Ctrl+C`.

5. Create the application user (connect as `SYSTEM` to the pluggable database):
    ```bash
    docker exec -it oracle-free sqlplus system/oraclepwd@FREEPDB1
    ```
    Then run:
    ```sql
    CREATE USER appuser IDENTIFIED BY apppassword;
    GRANT CONNECT TO appuser;
    GRANT CREATE TABLE TO appuser;
    GRANT UNLIMITED TABLESPACE TO appuser;
    ```

    Replace `appuser` and `apppassword` with your custom values (optional).

6. Next exit SQL*Plus, by typing `exit` in the console, and set the following environment variables:

    ```bash
    export JDBC_URL="jdbc:oracle:thin:@//localhost:1521/FREEPDB1"
    export JDBC_USER="appuser"
    export JDBC_PASSWORD="apppassword"
    ```
    Replacing the `appuser` and `apppassword` with your values.

Now you are all set to build and run this database application.

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

3. Run and test the application by passing some text as an argument (up to 200 characters):
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
      250a5862-21cd-4fb2-a152-4792b2da931b | Running a JAR            | 2026-04-07 17:21:43
      bf84d264-f5b0-4596-ac7e-279767776db3 | Running a native image   | 2026-04-07 17:14:27
      ```
4. (Optional) Stop the database after testing:
      ```bash
      docker stop oracle-free
      ```

Using the Oracle Database Free container is very easy, but it requires at least ~4 GB of available RAM and may start slowly on the first run.
Running the application as a native executable provides faster startup time and a lower memory footprint compared to running on the JVM.

## Clean the Table Data

If you run this demo multiple times, the table will keep growing.
To remove all rows from the demo table, connect to the database:
```bash
docker exec -it oracle-free sqlplus appuser/apppassword@FREEPDB1
```
Then reset:
```sql
TRUNCATE TABLE demo_events;
```

To drop the table entirely instead of clearing it:
```sql
DROP TABLE demo_events;
```

### Summary

The **Oracle JDBC driver works with GraalVM Native Image without any additional configuration** in this demo.
This works because the driver class (`oracle.jdbc.OracleDriver`) is explicitly referenced, and the application does not rely on dynamic class loading or reflection-heavy frameworks.
For a remote connection to a production database such as Oracle Autonomous Database, or Oracle Database on-prem, it will require extra steps for making this type of application "Native Image ready", which is demonstrated in separate projects in this repository.
