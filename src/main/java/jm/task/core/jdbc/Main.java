package jm.task.core.jdbc;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        // реализуйте алгоритм здесь
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();

        List<User> users = new ArrayList<>();
        users.add(new User("Алибек","Днишев",(byte)72));
        users.add(new User("Нагима","Ескалиева",(byte)69));
        users.add(new User("Ермек","Серкебаев",(byte)97));
        users.add(new User("Роза","Рымбаева",(byte)65));

        for (User user : users) {
            userService.saveUser(user.getName(), user.getLastName(), user.getAge());
            System.out.println("Пользователь – " + user.getName() + " " + user.getLastName() + " добавлен в базу данных");
        }

        List<User> usersTable = userService.getAllUsers();
        for (User user: userService.getAllUsers()) {
            System.out.println(user);
        }

        userService.cleanUsersTable();
        userService.dropUsersTable();

    }
}
