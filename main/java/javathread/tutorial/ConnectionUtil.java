package javathread.tutorial;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConnectionUtil {
//    private static final HikariDataSource hikariDataSource;
//
//    static {
//        HikariConfig config = new HikariConfig();
//        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        config.setJdbcUrl("jdbc:mysql://localhost:3306/library");
//        config.setUsername("root");
//        config.setPassword("root");
//
//        config.setMaximumPoolSize(10);
//        config.setMinimumIdle(5);
//        config.setIdleTimeout(60_000);
//        config.setMaxLifetime(60_000 * 10);
//
//        hikariDataSource = new HikariDataSource(config);
//    }
//    public static HikariDataSource getDataSource(){
//        return hikariDataSource;
//    }

    private static final HikariDataSource hikariDataSource;
    static {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/test/resources/jdbc.properties")) {
            properties.load(input);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(properties.getProperty("db.DriverClassName"));
        config.setJdbcUrl(properties.getProperty("db.jdbcUrl"));
        config.setUsername(properties.getProperty("db.userName"));
        config.setPassword(properties.getProperty("db.password"));

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(60_000);
        config.setMaxLifetime(60_000 * 10);

        hikariDataSource = new HikariDataSource(config);
    }

    public static HikariDataSource getDataSource(){
        return hikariDataSource;
    }
}
