package ru.geekbrains.krok.workers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.krok.workers.database.WorkersDB;
import ru.geekbrains.krok.workers.workersEntities.Workers;

import java.util.Scanner;


public class Main {

    static WorkersDB queries = new WorkersDB();
    static Scanner scanner = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger(Main.class.getName());


    public static void main(String[] args) {

        while (true) {
            try {

                queries.connectToDB();
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
                    case "addToDataBaseFromJson":
                        queries.addToDataBaseFromJson();
                        break;
                    case "addToJsonFromDataBase":
                        queries.addToJsonFromDataBase(queries.getEmployeeInfo());
                        break;
                    case "getEmployeeInfo":
                        System.out.println(queries.getEmployeeInfo());
                        break;
                    case "getAvgSalaryForAll":
                        System.out.println(queries.getAvgSalaryForAll());
                        break;
                    case "getAvgSalaryForPosition":
                        queries.getAvgSalaryForPosition(cmd[1]);
                        break;
                    case "searchEmployeeByPhone":
                        queries.searchEmployeeByPhone(cmd[1]);
                        break;
                    case "help":
                        System.out.println("addEmployee(String name, String position, double salary) \n" + "addEmployeeOtherInfo(String name, String phone, String address)  \n" + "getEmployeeInfo()  \n" + "getGetAvgSalaryForAll()  \n" + "getGetAvgSalaryForPosition(String position)  \n" + "searchEmployeeByPhone(String phone) \n" + "exit \n" + "help");
                        break;
                    case "exit":
                        queries.disconnect();
                        System.exit(0);
                    default:
                        System.out.println("Команды не существует! Для получения списка команда введите 'help'.");
                        break;
                }
            } catch (Exception e) {
                logger.error("Произошла ошибка! " + e);
                System.out.println("Команды не существует! Для получения списка команда введите 'help'");

            }


        }

    }
}
