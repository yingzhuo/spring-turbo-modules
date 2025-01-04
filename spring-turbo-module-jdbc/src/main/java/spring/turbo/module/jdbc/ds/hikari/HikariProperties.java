package spring.turbo.module.jdbc.ds.hikari;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;

import java.io.Serializable;

@Getter
@Setter
public class HikariProperties implements JdbcConnectionDetails, Serializable {

    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClassName;

    private String poolName = null;
    private int minimumIdle = 10;
    private int maximumPoolSize = 30;
    private boolean autoCommit = true;
    private long idleTimeout = 30000L;
    private long maxLifetime = 900000L;
    private long connectionTimeout = 10000;
    private String connectionTestQuery = "SELECT 1 FROM DUAL";
    private long validationTimeout = 1000L;

}
