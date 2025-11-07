package util;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.util.Properties;

public class DataSourceManager {

    public static HikariDataSource init() {
        try (InputStream in = DataSourceManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties p = new Properties();
            p.load(in);
//
            String driver = p.getProperty("db.driver");
            Class.forName(driver);
            String url = p.getProperty("db.url");
            String user = p.getProperty("db.user");
            String password = p.getProperty("db.password");
            HikariConfig cfg = new HikariConfig();
            cfg.setJdbcUrl(url);
            cfg.setUsername(user);
            cfg.setPassword(password);
            cfg.setMaximumPoolSize(10);
            cfg.setPoolName("app-pool");
            return new HikariDataSource(cfg);
        } catch (Exception e) {
            throw new RuntimeException("Failed to init DataSourceManager", e);
        }
    }
}
