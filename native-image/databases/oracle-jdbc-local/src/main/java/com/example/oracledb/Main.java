package com.example.oracledb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.util.UUID;

public final class Main {

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
    // Keeps the Oracle JDBC driver explicitly reachable during Native Image analysis
    Class.forName("oracle.jdbc.OracleDriver");

    String jdbcUrl = requireEnv("JDBC_URL");
    String jdbcUser = requireEnv("JDBC_USER");
    String jdbcPassword = requireEnv("JDBC_PASSWORD");
    String message = args.length > 0 ? String.join(" ", args) : "Stored from Java run";
    String eventId = UUID.randomUUID().toString();

    try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) {
      connection.setAutoCommit(false);

      ensureTableExists(connection);
      insertEvent(connection, eventId, message);
      int rowCount = countEvents(connection);

      connection.commit();

      System.out.println("Connected to Oracle Database.");
      System.out.printf("Inserted row with event_id=%s and message=%s%n", eventId, message);
      System.out.printf("demo_events row count: %d%n", rowCount);
      printLatestEvents(connection);
    }
  }

  private static void ensureTableExists(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      statement.execute(CREATE_TABLE_SQL);
    }
  }

  private static void insertEvent(Connection connection, String eventId, String message) throws SQLException {
    String sql = "INSERT INTO demo_events (event_id, message, created_at) VALUES (?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, eventId);
      statement.setString(2, message);
      statement.setTimestamp(3, java.sql.Timestamp.from(Instant.now()));
      statement.executeUpdate();
    }
  }

  private static int countEvents(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM demo_events")) {
      resultSet.next();
      return resultSet.getInt(1);
    }
  }

  private static void printLatestEvents(Connection connection) throws SQLException {
    String sql = "SELECT event_id, message, created_at FROM demo_events ORDER BY created_at DESC FETCH FIRST 5 ROWS ONLY";
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
  }

  private static String requireEnv(String name) {
    String value = System.getenv(name);
    if (value == null || value.isBlank()) {
      throw new IllegalStateException("Missing required environment variable: " + name);
    }
    return value;
  }
}
