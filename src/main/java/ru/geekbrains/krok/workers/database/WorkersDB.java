package ru.geekbrains.krok.workers.database;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.krok.workers.workersEntities.Workers;

import java.sql.*;


public class WorkersDB {

    Connection connection = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    private static final Logger logger = LogManager.getLogger(Workers.class.getName());

    public WorkersDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:workers.db");
            logger.info("Подключение создано успешно!");

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Возникли проблемы при подключении к базе данных!");
            new RuntimeException("Невозможно подключиться к базе данных");

        }
    }

    public void addEmployee(String name, String position, double salary) {

        try {
            pstm = connection.prepareStatement("Insert into employees (name, position, salary) values (?,?,?)");
            pstm.setString(1, name);
            pstm.setString(2, position);
            pstm.setDouble(3, salary);
            pstm.execute();
            logger.info("addEmployee успешно выполнен!");

        } catch (SQLException e) {
            logger.error("addEmployee завершился с ошибкой!");
            e.printStackTrace();
        }
    }

    public void addEmployeeOtherInfo(String name, String phone, String address) {

        try {

            pstm = connection.prepareStatement("Insert into otherinformation (id, phone, address) values ((select Max(e.id) from employees e\n" +
                    "left join otherinformation oi on e.id = oi.id\n" +
                    "where e.name = ?),?,?)");
            pstm.setString(1, name);
            pstm.setString(2, phone);
            pstm.setString(3, address);
            pstm.execute();
            logger.info("addEmployeeOtherInfo успешно выполнен!");

        } catch (SQLException e) {
            logger.error("addEmployeeOtherInfo завершился с ошибкой!");
            e.printStackTrace();
        }


    }


    public ResultSet getEmployeeInfo() {


        try {
            pstm = connection.prepareStatement("select * from employees");
            rs = pstm.executeQuery();
            logger.info("getEmployeeInfo успешно выполнен!");
        } catch (SQLException e) {
            logger.error("getEmployeeInfo завершился с ошибкой!");
            e.printStackTrace();
        }
        return rs;


    }


    public ResultSet getGetAvgSalaryForAll() {


        try {
            pstm = connection.prepareStatement("select AVG(salary) from employees");
            rs = pstm.executeQuery();
            logger.info("getGetAvgSalaryForAll успешно выполнен!");
        } catch (SQLException e) {
            logger.error("getEmployeeInfo завершился с ошибкой!");
            e.printStackTrace();
        }

        return rs;

    }


    public ResultSet getGetAvgSalaryForPosition(String position) {


        try {
            pstm = connection.prepareStatement("select name, AVG(salary) from employees e\n" +
                    "where position = ?\n" +
                    "group by name;");
            pstm.setString(1, position);
            rs = pstm.executeQuery();
            logger.info("getGetAvgSalaryForPosition успешно выполнен!");
        } catch (SQLException e) {
            logger.error("getGetAvgSalaryForPosition завершился с ошибкой!");
            e.printStackTrace();

        }
        return rs;


    }

    public ResultSet searchEmployeeByPhone(String phone) throws SQLException {

        try {
            pstm = connection.prepareStatement("select * from employees e\n" +
                    "left join otherinformation oi on e.id = oi.id\n" +
                    "where oi.phone like ?");
            pstm.setString(1, phone + "%");
            rs = pstm.executeQuery();
            logger.info("searchEmployeeByPhone успешно выполнен!");
        } catch (SQLException e) {
            logger.error("searchEmployeeByPhone завершился с ошибкой!");
            e.printStackTrace();
        }
        return rs;
    }

    public void disconnect() {
        try {
            if (!rs.isClosed() || rs != null) {
                rs.close();
            }

            if (!pstm.isClosed() || pstm != null) {
                pstm.close();
            }
            if (!connection.isClosed() || connection != null) {
                connection.close();
            }
            logger.info("Подключение разорвано успешно!");

        } catch (SQLException e) {
            logger.error("Возникли проблемы при разрыве подключения!");
            e.printStackTrace();
        }
    }

}
