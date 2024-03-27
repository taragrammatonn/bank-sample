package md.maib.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class JdbcConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private static JdbcConfig instance;

    public JdbcConfig(){
        instance = this;
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException{
        if(instance == null){
            throw new IllegalStateException("JdbcConfig instance has not been initialized by Spring");
        }
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(instance.url, instance.username, instance.password);
    }
}
