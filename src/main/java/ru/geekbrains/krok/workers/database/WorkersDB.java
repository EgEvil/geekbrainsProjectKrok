package ru.geekbrains.krok.workers.database;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.krok.workers.workersEntities.Workers;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.*;


public class WorkersDB {

    Connection connection = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    private static final Logger logger = LogManager.getLogger(Workers.class.getName());

    public WorkersDB() {

    }

    public Connection connectToDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:workers.db");
            logger.info("Подключение создано успешно!");

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Возникли проблемы при подключении к базе данных! " + e);


        }
        return connection;
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
            logger.error("addEmployee завершился с ошибкой! " + e);

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
            logger.error("addEmployeeOtherInfo завершился с ошибкой! " + e);

        }


    }

    public void addToDataBaseFromJson() {

        try {

            ObjectMapper workersMapper = new ObjectMapper();
            Workers[] workersList = workersMapper.readValue(new File("workersToDB.json"), Workers[].class);

            for (Workers addWorker : workersList) {
                addEmployee(addWorker.getName(), addWorker.getPosition(), addWorker.getSalary());

            }

            logger.info("addToDataBaseFromJson успешно выполнен!");
        } catch (
                IOException e) {
            logger.error("addToDataBaseFromJson завершился с ошибкой! " + e);

        }

    }


    public void addToJsonFromDataBase(List<Workers> workersList) {


        ObjectMapper workersMapper = new ObjectMapper();
        workersMapper.enable(SerializationFeature.INDENT_OUTPUT);


        try {
            workersMapper.writeValue(new File("workers.json"), workersList);
            logger.info("addToJsonFromDataBase успешно выполнен!");
        } catch (IOException e) {
            logger.error("addToJsonFromDataBase завершился с ошибкой! " + e);
        }


    }


    public List<Workers> getEmployeeInfo() {

        List<Workers> workersList = new ArrayList<>();

        try {
            pstm = connection.prepareStatement("select * from employees");
            rs = pstm.executeQuery();
            while (rs.next()) {
                Workers workers = new Workers();
                workers.setId(rs.getInt("id"));
                workers.setName(rs.getString("name"));
                workers.setPosition(rs.getString("position"));
                workers.setSalary(rs.getDouble("salary"));
                workersList.add(workers);
            }

            logger.info("getEmployeeInfo успешно выполнен!");
        } catch (SQLException e) {
            logger.error("getEmployeeInfo завершился с ошибкой! " + e);

        }
        return workersList;


    }


    public double getAvgSalaryForAll() {

        Workers workers = null;
        try {
            pstm = connection.prepareStatement("select AVG(salary) from employees");
            rs = pstm.executeQuery();
            while (rs.next()) {
                workers = new Workers();
                workers.setSalary(rs.getDouble("AVG(salary)"));

            }
            logger.info("getGetAvgSalaryForAll успешно выполнен!");
        } catch (SQLException e) {
            logger.error("getEmployeeInfo завершился с ошибкой! " + e);
        }

        return workers.getSalary();

    }


    public void getAvgSalaryForPosition(String position) {


        try {
            pstm = connection.prepareStatement("select name, AVG(salary) from employees e\n" +
                    "where position = ?\n" +
                    "group by name;");
            pstm.setString(1, position);
            rs = pstm.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("name") + rs.getDouble("AVG(salary)"));
            }
            logger.info("getGetAvgSalaryForPosition успешно выполнен!");
        } catch (SQLException e) {
            logger.error("getGetAvgSalaryForPosition завершился с ошибкой! " + e);

        }

    }

    public void searchEmployeeByPhone(String phone) throws SQLException {

        try {
            pstm = connection.prepareStatement("select * from employees e\n" +
                    "left join otherinformation oi on e.id = oi.id\n" +
                    "where oi.phone like ?");
            pstm.setString(1, phone + "%");
            rs = pstm.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt("id") +
                        rs.getString("name") +
                        rs.getString("position") +
                        rs.getDouble("salary"));
            }
            logger.info("searchEmployeeByPhone успешно выполнен!");
        } catch (SQLException e) {
            logger.error("searchEmployeeByPhone завершился с ошибкой! " + e);
            e.printStackTrace();
        }

    }

    public void disconnect() {
        try {
            if (!rs.isClosed() || rs != null) {
                rs.close();
                logger.info("ResultSet успешно закрыт!");
            }
        } catch (SQLException e) {
            logger.error("Возникли проблемы при закрытие подключения (ResultSet)! " + e);
        }

        try {
            if (!pstm.isClosed() || pstm != null) {
                pstm.close();
                logger.info("PrepareStatement успешно закрыт!");
            }
        } catch (SQLException e) {
            logger.error("Возникли проблемы при закрытие подключения (PrepareStatement)! " + e);
        }

        try {
            if (!connection.isClosed() || connection != null) {
                connection.close();
                logger.info("Подключение закрыто успешно!");
            }
        } catch (SQLException e) {
            logger.error("Возникли проблемы при закрытие подключения (Connection)! " + e);

        }


    }

}


