package Kornienko.test;

import Kornienko.test.model.User;
import Kornienko.test.model.UserRequest;
import Kornienko.test.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.CollectionUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserRepositoryTests.class)
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test")
public class UserRepositoryTests {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer1;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer2;

    @Autowired
    private UserRepository userRepository;

    static {
        postgreSQLContainer1 = new PostgreSQLContainer("postgres:17.2");
        postgreSQLContainer1.withUsername("testuser");
        postgreSQLContainer1.withPassword("testpass");
        postgreSQLContainer1.withDatabaseName("data-base-1");
        postgreSQLContainer1.withInitScript("db-init/data-base-1.sql");
        postgreSQLContainer1.setPortBindings(List.of("5434:5432"));
        postgreSQLContainer1.start();

        postgreSQLContainer2 = new PostgreSQLContainer("postgres:17.2");
        postgreSQLContainer2.withUsername("testuser");
        postgreSQLContainer2.withPassword("testpass");
        postgreSQLContainer2.withDatabaseName("data-base-2");
        postgreSQLContainer2.withInitScript("db-init/data-base-2.sql");
        postgreSQLContainer2.setPortBindings(List.of("5435:5432"));
        postgreSQLContainer2.start();
    }

    @Test
    public void getUsers_allUsers() {
        List<User> users = userRepository.getUsers(null);
        assertThat(users.size()).isEqualTo(5);
        Assertions.assertThat(users).hasSameElementsAs(getAllUsers());
    }

    @Test
    public void getUsers_filterById_exists() {
        UserRequest userRequest = new UserRequest();
        userRequest.setFilters(Map.of("id", "user-id-1"));
        List<User> users = userRepository.getUsers(userRequest);
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getId()).isEqualTo("user-id-1");
    }

    @Test
    public void getUsers_filterById_notExists() {
        UserRequest userRequest = new UserRequest();
        userRequest.setFilters(Map.of("id", "user-id-0"));
        List<User> users = userRepository.getUsers(userRequest);
        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    public void getUsers_filterByNameAndSurname_exists() {
        UserRequest userRequest = new UserRequest();
        userRequest.setFilters(Map.of("name", "Mikasa", "surname", "Ackerman"));
        List<User> users = userRepository.getUsers(userRequest);
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getName()).isEqualTo("Mikasa");
        assertThat(users.get(0).getSurname()).isEqualTo("Ackerman");
    }

    @Test
    public void getUsers_filterByNameAndSurname_notExists() {
        UserRequest userRequest = new UserRequest();
        userRequest.setFilters(Map.of("name", "Ervin", "surname", "Sadis"));
        List<User> users = userRepository.getUsers(userRequest);
        assertThat(users.size()).isEqualTo(0);
    }


    private List<User> getAllUsers() {
        List<User> usersExpected = new ArrayList<>();

        User user = new User();
        user.setId("user-id-1");
        user.setUsername("user-1");
        user.setName("Ervin");
        user.setSurname("Smit");
        usersExpected.add(user);

        user = new User();
        user.setId("user-id-2");
        user.setUsername("user-2");
        user.setName("Kit");
        user.setSurname("Sadis");
        usersExpected.add(user);

        user = new User();
        user.setId("user-id-3");
        user.setUsername("user-3");
        user.setName("Levi");
        user.setSurname("Ackerman");
        usersExpected.add(user);

        user = new User();
        user.setId("user-ldap-1");
        user.setUsername("user-ldap-1");
        user.setName("Eren");
        user.setSurname("Jeger");
        usersExpected.add(user);

        user = new User();
        user.setId("user-ldap-2");
        user.setUsername("user-ldap-2");
        user.setName("Mikasa");
        user.setSurname("Ackerman");
        usersExpected.add(user);

        return usersExpected;
    }
}
