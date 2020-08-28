package alex.ork.springboot.course.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.logging.Logger;

@Configuration
@ComponentScan(basePackages = "alex.ork.springboot.course.config")
@PropertySource("classpath:application.properties")
public class DataSourceConfig {

    @Autowired
    private Environment environment;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Bean
    public DataSource securityDataSource() {
        // create connection pull
        ComboPooledDataSource securityDataSource = new ComboPooledDataSource();

        // set the jdbc driver class
        try {
            securityDataSource.setDriverClass(environment.getProperty("jdbc.driver"));
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }

        logger.info("jdbc.url=" + environment.getProperty("spring.datasource.url"));
        logger.info("jdbc.user=" + environment.getProperty("spring.datasource.username"));

        // set database connection props
        securityDataSource.setJdbcUrl(environment.getProperty("spring.datasource.url"));
        securityDataSource.setUser(environment.getProperty("spring.datasource.username"));
        securityDataSource.setPassword(environment.getProperty("spring.datasource.password"));

        // set connection pool props
        securityDataSource.setInitialPoolSize(
                getIntProperty("connection.pool.initialPoolSize"));
        securityDataSource.setMinPoolSize(
                getIntProperty("connection.pool.minPoolSize"));
        securityDataSource.setMaxPoolSize(
                getIntProperty("connection.pool.maxPoolSize"));
        securityDataSource.setMaxIdleTime(
                getIntProperty("connection.pool.maxIdleTime"));

        return securityDataSource;
    }

    // read environment property and convert to int
    private int getIntProperty(String propName) {
        String propVal = environment.getProperty(propName);
        int intPropVal = Integer.parseInt(propVal);
        return intPropVal;
    }
}
