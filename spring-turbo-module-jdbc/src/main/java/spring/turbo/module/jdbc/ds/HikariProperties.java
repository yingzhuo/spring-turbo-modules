package spring.turbo.module.jdbc.ds;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;

import java.io.Serializable;

/**
 * @author 应卓
 * @since 3.4.1
 */
@Getter
@Setter
@ToString
public class HikariProperties implements JdbcConnectionDetails, Serializable {

    private String driverClassName;
    private String jdbcUrl;
    private String username;
    private String password;
    private String poolName = "Hikari";
    private int minimumIdle = 10;
    private int maximumPoolSize = 30;
    private boolean autoCommit = true;
    private long idleTimeout = 30000L;
    private long maxLifetime = 900000L;
    private long connectionTimeout = 10000;
    private String connectionTestQuery = null;
    private long validationTimeout = 1000L;
    private String connectionInitSql = null;
    private long initializationFailTimeout = 1000L;

}
