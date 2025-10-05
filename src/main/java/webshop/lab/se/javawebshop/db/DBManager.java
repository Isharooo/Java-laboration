package webshop.lab.se.javawebshop.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager {

    private static DBManager instance;
    private String url;
    private String username;
    private String password;
    private String driver;

    private DBManager() {
        loadProperties();
        loadDriver();
    }

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    private void loadProperties() {
        Properties props = new Properties();

        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new RuntimeException("Kunde inte hitta db.properties i classpath");
            }

            props.load(input);

            this.url = props.getProperty("db.url");
            this.username = props.getProperty("db.username");
            this.password = props.getProperty("db.password");
            this.driver = props.getProperty("db.driver");

            if (url == null || username == null || password == null || driver == null) {
                throw new RuntimeException("db.properties måste innehålla: db.url, db.username, db.password, db.driver");
            }

            System.out.println("Databasegenskaper laddade från db.properties");

        } catch (IOException e) {
            throw new RuntimeException("Fel vid läsning av db.properties: " + e.getMessage(), e);
        }
    }

    private void loadDriver() {
        try {
            Class.forName(driver);
            System.out.println("JDBC-driver laddad: " + driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Kunde inte ladda JDBC-driver: " + driver, e);
        }
    }

    public Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Databasanslutning skapad");
            return conn;
        } catch (SQLException e) {
            System.err.println("Fel vid anslutning till databas: " + e.getMessage());
            throw e;
        }
    }

    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Databasanslutning stängd");
            } catch (SQLException e) {
                System.err.println("Fel vid stängning av anslutning: " + e.getMessage());
            }
        }
    }

    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Anslutningstest misslyckades: " + e.getMessage());
            return false;
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }
}