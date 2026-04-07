package com.example.oracledb;

import oracle.security.pki.OraclePKIProvider;
import oracle.ucp.UniversalConnectionPoolException;
import oracle.ucp.admin.UniversalConnectionPoolManager;
import oracle.ucp.admin.UniversalConnectionPoolManagerImpl;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

import java.security.Security;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Main {
  private static final String POOL_NAME = "messages-store-pool";

  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

  private static final String CREATE_TABLE_SQL = """
      BEGIN
        EXECUTE IMMEDIATE '
          CREATE TABLE demo_events (
            event_id VARCHAR2(36) PRIMARY KEY,
            message VARCHAR2(200) NOT NULL,
            created_at TIMESTAMP NOT NULL
          )';
      EXCEPTION
        WHEN OTHERS THEN
          IF SQLCODE != -955 THEN
            RAISE;
          END IF;
      END;
      """;

  private Main() {
  }

  public static void main(String[] args) throws Exception {
    Security.addProvider(new OraclePKIProvider());
    Class.forName("oracle.jdbc.OracleDriver");

    String jdbcUrl = requireEnv("JDBC_URL");
    String jdbcUser = requireEnv("JDBC_USER");
    String jdbcPassword = requireEnv("JDBC_PASSWORD");
    List<String> messages = buildMessages(args);

    PoolDataSource dataSource = PoolDataSourceFactory.getPoolDataSource();
    dataSource.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
    dataSource.setURL(jdbcUrl);
    dataSource.setUser(jdbcUser);
    dataSource.setPassword(jdbcPassword);
    dataSource.setConnectionPoolName(POOL_NAME);
    dataSource.setInitialPoolSize(1);
    dataSource.setMinPoolSize(1);
    dataSource.setMaxPoolSize(4);

    try {
      ensureTableExists(dataSource);

      for (String message : messages) {
        String eventId = UUID.randomUUID().toString();
        insertEvent(dataSource, eventId, message);
        System.out.printf("Inserted row with event_id=%s and message=%s%n", eventId, message);
      }

      int rowCount = countEvents(dataSource);
      System.out.println("Connected to Oracle Database through Oracle UCP.");
      System.out.printf("demo_events row count: %d%n", rowCount);
      printPoolStats(dataSource);
      printLatestEvents(dataSource);
    } finally {
      destroyPoolIfInitialized();
    }
  }

  private static List<String> buildMessages(String[] args) {
    List<String> messages = new ArrayList<>();
    if (args.length == 0) {
      messages.add("Stored from pooled Java run #1");
      messages.add("Stored from pooled Java run #2");
      messages.add("Stored from pooled Java run #3");
      return messages;
    }
    for (String arg : args) {
      messages.add(arg);
    }
    return messages;
  }

  private static void ensureTableExists(PoolDataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      try (Statement statement = connection.createStatement()) {
      statement.execute(CREATE_TABLE_SQL);
      }
      connection.commit();
    }
  }

  private static void insertEvent(PoolDataSource dataSource, String eventId, String message) throws SQLException {
    String sql = "INSERT INTO demo_events (event_id, message, created_at) VALUES (?, ?, ?)";
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, eventId);
      statement.setString(2, message);
      statement.setTimestamp(3, java.sql.Timestamp.from(Instant.now()));
      statement.executeUpdate();
      }
      connection.commit();
    }
  }

  private static int countEvents(PoolDataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      int rowCount;
      try (Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM demo_events")) {
        resultSet.next();
        rowCount = resultSet.getInt(1);
      }
      connection.commit();
      return rowCount;
    }
  }

  private static void printLatestEvents(PoolDataSource dataSource) throws SQLException {
    String sql = "SELECT event_id, message, created_at FROM demo_events ORDER BY created_at DESC FETCH FIRST 5 ROWS ONLY";
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      try (Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery(sql)) {
        System.out.println("Latest rows:");
        System.out.println("event_id                              | message                  | created_at");
        System.out.println("--------------------------------------+--------------------------+-------------------");
        while (resultSet.next()) {
          System.out.printf("%s | %-24s | %s%n",
              resultSet.getString("event_id"),
              resultSet.getString("message"),
              TIMESTAMP_FORMATTER.format(resultSet.getTimestamp("created_at").toInstant()));
        }
      }
      connection.commit();
    }
  }

  private static void printPoolStats(PoolDataSource dataSource) throws SQLException {
    System.out.printf("Oracle UCP pool '%s': available=%d, borrowed=%d, total=%d%n",
        dataSource.getConnectionPoolName(),
        dataSource.getAvailableConnectionsCount(),
        dataSource.getBorrowedConnectionsCount(),
        dataSource.getAvailableConnectionsCount() + dataSource.getBorrowedConnectionsCount());
  }

  private static void destroyPoolIfInitialized() throws UniversalConnectionPoolException {
    UniversalConnectionPoolManager manager = UniversalConnectionPoolManagerImpl.getUniversalConnectionPoolManager();
    for (String poolName : manager.getConnectionPoolNames()) {
      if (POOL_NAME.equals(poolName)) {
        manager.destroyConnectionPool(POOL_NAME);
        break;
      }
    }
  }

  private static String requireEnv(String name) {
    String value = System.getenv(name);
    if (value == null || value.isBlank()) {
      throw new IllegalStateException("Missing required environment variable: " + name);
    }
    return value;
  }
}
