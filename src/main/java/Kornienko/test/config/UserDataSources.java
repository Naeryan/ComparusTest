package Kornienko.test.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserDataSources {

    private List<HikariDataSource> ds = new ArrayList<>();

    @Autowired
    public UserDataSources(DataSourceConfig dataSourceConfig) {
        if (!dataSourceConfig.getDataSources().isEmpty()) {
            dataSourceConfig.getDataSources().forEach(dataSource -> {
                HikariConfig config = new HikariConfig();
                config.setDriverClassName(org.postgresql.Driver.class.getName());
                config.setJdbcUrl(dataSource.getUrl() + "/" + dataSource.getName());
                config.setUsername(dataSource.getUser());
                config.setPassword(dataSource.getPassword());
                ds.add(new HikariDataSource(config));
            });
        }
    }

    public List<Connection> getConnections() {
        return ds.stream()
                .map(dataSource -> {
                    try {
                        return dataSource.getConnection();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

}
