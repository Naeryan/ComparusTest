package Kornienko.test.repository;

import Kornienko.test.config.DataSourceConfig;
import Kornienko.test.config.UserDataSources;
import Kornienko.test.model.DataSource;
import Kornienko.test.model.User;
import Kornienko.test.model.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Repository
public class UserRepository {

    private final DataSourceConfig dataSourceConfig;

    @Autowired
    private UserDataSources userDataSources;

    public UserRepository(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    public List<User> getUsers(UserRequest userRequest) {
        List<User> users = new ArrayList<>();
        try {
            List<Connection> connections = userDataSources.getConnections();
            for (Connection connection : connections) {
                users.addAll(getUsersFromDb(connection, userRequest));
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    private List<User> getUsersFromDb(Connection connection, UserRequest userRequest) throws SQLException {
        String url = connection.getMetaData().getURL();
        DataSource dataSource = dataSourceConfig.getDataSources().stream()
                .filter(config -> url.startsWith(config.getUrl()))
                .findAny().orElseThrow(SQLException::new);

        List<User> users = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    getUserSQL(dataSource.getTable(), dataSource.getMapping(), userRequest));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getString("id"));
                user.setUsername(resultSet.getString("username"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                users.add(user);
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    private String getUserSQL(String tableName, Map<String, String> mapping, UserRequest userRequest) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        if (mapping.containsKey("id")) {
            query.append(mapping.get("id")).append(" AS id, ");
        }
        if (mapping.containsKey("username")) {
            query.append(mapping.get("username")).append(" AS username, ");
        }
        if (mapping.containsKey("name")) {
            query.append(mapping.get("name")).append(" AS name, ");
        }
        if (mapping.containsKey("surname")) {
            query.append(mapping.get("surname")).append(" AS surname ");
        }
        query.append("FROM ").append(tableName);

        if (Objects.nonNull(userRequest) && !userRequest.getFilters().isEmpty() &&
                userRequest.getFilters().keySet().stream().anyMatch(mapping::containsKey)) {
            AtomicBoolean isFirst = new AtomicBoolean(true);
            query.append(" WHERE ");
            userRequest.getFilters().forEach((key, value) -> {
                if (mapping.containsKey(key)) {
                    if (!isFirst.get()) {
                        query.append(mapping.get(key)).append(" AND ");
                    } else {
                        isFirst.set(false);
                    }
                    query.append(mapping.get(key)).append(" = '").append(value).append("' ");
                }
            });
        }
        return query.toString();
    }

}
