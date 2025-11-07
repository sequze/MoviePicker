package util;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.InputStream;
import java.util.Properties;

public class DataSourceManager {

    public static HikariDataSource init() {
        try {
            Dotenv dotenv = Dotenv.load();
            String driver = dotenv.get("DB_DRIVER");
            Class.forName(driver);
            String url = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");
            HikariConfig cfg = new HikariConfig();
            cfg.setJdbcUrl(url);
            cfg.setUsername(user);
            cfg.setPassword(password);
            cfg.setMaximumPoolSize(10);
            cfg.setPoolName("app-pool");
            return new HikariDataSource(cfg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
