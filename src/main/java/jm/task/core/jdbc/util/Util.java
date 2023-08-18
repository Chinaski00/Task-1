package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    // реализуйте настройку соединения с БД
    private static final String URL_KEY = "db.url";
    private static final String USER_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";
    public static final String JDBC_KEY = "db.jdbc";
    public static final String DIALECT_KEY = "db.dialect";
    public static final String SHOW_SQL_KEY = "db.show_sql";


    public Util() {
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USER_KEY),
                    PropertiesUtil.get(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }

    private static final SessionFactory sessionFactory = buildSessionFactory();
    private Session session;
    private Transaction transaction;

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .setProperty("hibernate.connection.driver_class", PropertiesUtil.get(JDBC_KEY))
                    .setProperty("hibernate.connection.url", PropertiesUtil.get(URL_KEY))
                    .setProperty("hibernate.connection.username", PropertiesUtil.get(USER_KEY))
                    .setProperty("hibernate.connection.password", PropertiesUtil.get(PASSWORD_KEY))
                    .setProperty("hibernate.dialect", PropertiesUtil.get(DIALECT_KEY))
                    .setProperty("hibernate.show_sql", PropertiesUtil.get(SHOW_SQL_KEY))
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        } catch (Throwable e) {
            System.err.println("Ошибка создания SessionFactory" + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }



    public Session getSession() {
        return session;
    }


    public Session openSession() {
        return getSessionFactory().openSession();
    }

    public Session openTransactionSession() {
        session = openSession();
        transaction = session.beginTransaction();
        return session;
    }

    public void closeSession() {
        session.close();
    }

    public void closeTransactionSession() {
        transaction.commit();
        closeSession();
    }


    final public static class PropertiesUtil {
        public static final Properties PROPERTIES = new Properties();


        static {
            loadProperties();
        }

        public PropertiesUtil() {
        }

        public static String get(String key) {
            return PROPERTIES.getProperty(key);
        }

        public static void loadProperties() {
            try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
                PROPERTIES.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
