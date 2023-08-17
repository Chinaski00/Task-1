package jm.task.core.jdbc.dao;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        var sql = """
                CREATE TABLE IF NOT EXISTS users
                (
                id SERIAL PRIMARY KEY,
                name VARCHAR(128) NOT NULL,
                last_name VARCHAR(128) NOT NULL,
                age INTEGER
                );
                """;
        try (Connection connection = Util.open();
        var statement = connection.createStatement()){
            statement.executeUpdate(sql);
            System.out.println("Таблица создана!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        var sql = "DROP TABLE IF EXISTS users";
        try (Connection connection = Util.open();
        var statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Таблица удалена!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        var sql = """
                INSERT INTO users
                (name, last_name, age)
                VALUES(?, ?, ?)
                """;
        try (Connection connection = Util.open();
        var prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setString(1,name);
            prepareStatement.setString(2,lastName);
            prepareStatement.setInt(3,age);
            prepareStatement.executeUpdate();
            System.out.println("Пользователь добавлен!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        var sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = Util.open();
        var prepareStatement = connection.prepareStatement(sql)){

            prepareStatement.setLong(1, id);
            prepareStatement.executeUpdate();
            System.out.println("Пользователь удалён!");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        var sql = """
                SELECT
                id,
                name,
                last_name,
                age
                FROM users
                """;
        try (Connection connection = Util.open();
        var statement = connection.createStatement()){

            var resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }
            System.out.println("Список пользователей готов");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        var sql = """
                DO $$
                BEGIN
                IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'users') THEN
                DELETE FROM users;
                END IF;
                END
                $$
                """;
        try (Connection connection = Util.open();
             var statement = connection.createStatement()) {

            statement.executeUpdate(sql);
            System.out.println("Таблица очищена");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
