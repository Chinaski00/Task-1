package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Util  {
    // реализуйте настройку соединения с БД
    private static final String URL_KEY = "db.url";
    private static final String USER_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";


    private Util() {
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(PropertiesUtil.get(URL_KEY), PropertiesUtil.get(USER_KEY), PropertiesUtil.get(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }

}
