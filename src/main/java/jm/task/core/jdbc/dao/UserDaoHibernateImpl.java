package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ThreadLocalSessionContext;

import java.util.ArrayList;
import java.util.List;


public class UserDaoHibernateImpl extends Util implements UserDao {
    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        openTransactionSession();
        String sql = """
                CREATE TABLE IF NOT EXISTS users
                (
                id BIGSERIAL PRIMARY KEY ,
                name VARCHAR(128) NOT NULL,
                last_name VARCHAR(128) NOT NULL,
                age INTEGER
                );
                """;
        var session = getSession();
        session.createSQLQuery(sql).executeUpdate();
        closeTransactionSession();
        System.out.println("Создана таблица");
    }

    @Override
    public void dropUsersTable() {
        openTransactionSession();
        var session = getSession();
        String sql = """
                DROP TABLE IF EXISTS users
                """;
        session.createSQLQuery(sql).executeUpdate();
        closeTransactionSession();
        System.out.println("Таблица удалена!");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        openTransactionSession();
        var session = getSession();
        String sql = """
                INSERT INTO users (name, last_name, age) VALUES(:name, :lastName, :age)
                """;
        session.createSQLQuery(sql)
                .setParameter("name", name)
                .setParameter("lastName", lastName)
                .setParameter("age", age)
                .executeUpdate();
        closeTransactionSession();
        System.out.println("Пользователь добавлен!");
    }

    @Override
    public void removeUserById(long id) {
        openTransactionSession();

        var sql = "DELETE FROM users WHERE id = :id";
        var session = getSession();

        session.createSQLQuery(sql).setParameter("id", id).executeUpdate();
        closeTransactionSession();
        System.out.println("Пользователь удален");
    }

    @Override
    public List<User> getAllUsers() {
        openTransactionSession();
        String sql = """
                SELECT
                id,
                name,
                last_name,
                age
                FROM users
                """;
        var session = getSession();
        List<User> userList = session.createSQLQuery(sql).addEntity(User.class).list();
        closeTransactionSession();
        System.out.println("Список пользователей готов");
        return userList;

    }

    @Override
    public void cleanUsersTable() {
        openTransactionSession();
        String sql = "DELETE FROM users";
        var session = getSession();
        session.createSQLQuery(sql).executeUpdate();
        closeTransactionSession();
        System.out.println("Таблица очищена");
    }
}
