package ru.geekbrains.krok.workers;

import org.apache.logging.log4j.LogManager;
import ru.geekbrains.krok.workers.database.WorkersDB;
import ru.geekbrains.krok.workers.workersEntities.Workers;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {

    static WorkersDB queries = new WorkersDB();
    static Scanner scanner = new Scanner(System.in);
    static ResultSet rs = null;
    static List<Workers> workersList = new ArrayList();


    public static void main(String[] args) {

        while (true) {
            try {

                System.out.println("Введите команду и параметры(Пример: cmd, 2, 4): ");
                String in = scanner.nextLine();
                String[] cmd = in.split(", ");
                switch (cmd[0]) {
                    case "addEmployee":
                        queries.addEmployee(cmd[1], cmd[2], Double.valueOf(cmd[3]));
                        break;
                    case "addEmployeeOtherInfo":
                        queries.addEmployeeOtherInfo(cmd[1], cmd[2], cmd[3]);
                        break;
                    case "getEmployeeInfo":
                        rs = queries.getEmployeeInfo();
                        while (rs.next()) {
                            workersList.add(new Workers(rs.getInt("id"),
                                    rs.getString("name"),
                                    rs.getString("position"),
                                    rs.getDouble("salary")));
                        }
                        System.out.println(workersList);

                        break;
                    case "getGetAvgSalaryForAll":
                        rs = queries.getGetAvgSalaryForAll();
                        while (rs.next()) {
                            System.out.println(rs.getDouble("AVG(salary)"));

                        }
                        break;
                    case "getGetAvgSalaryForPosition":
                        rs = queries.getGetAvgSalaryForPosition(cmd[1]);
                        while (rs.next()) {
                            System.out.println(rs.getString("name") + rs.getDouble("AVG(salary)"));
                        }
                        break;
                    case "searchEmployeeByPhone":
                        rs = queries.searchEmployeeByPhone(cmd[1]);
                        while (rs.next()) {
                            System.out.println(rs.getInt("id") +
                                    rs.getString("name") +
                                    rs.getString("position") +
                                    rs.getDouble("salary"));
                        }
                        break;
                    case "help":
                        help();
                        break;
                    case "exit":
                        queries.disconnect();
                        System.exit(0);
                    default:
                        System.out.println("Команды не существует! Для получения списка команда введите 'help'.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Команды не существует! Для получения списка команда введите 'help'.");
                help();
            }


        }

    }

    public static void help() {

        String[] methodsArr = {"addEmployee(String name, String position, double salary)", "addEmployeeOtherInfo(String name, String phone, String address)", "getEmployeeInfo()", "getGetAvgSalaryForAll()", "getGetAvgSalaryForPosition(String position)", "searchEmployeeByPhone(String phone)", "exit", "help"};

        System.out.println("Программа содержит следущие методы: ");
        for (int i = 0; i < methodsArr.length; i++) {

            System.out.println(methodsArr[i]);

        }
    }
}