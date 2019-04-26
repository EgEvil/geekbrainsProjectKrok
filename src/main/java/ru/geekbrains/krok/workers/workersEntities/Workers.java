package ru.geekbrains.krok.workers.workersEntities;


public class Workers {

    private int id;
    private String name;
    private String position;
    private double salary;

    public Workers() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

     public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "\n ID: " + getId() + " Name: " + getName() + " Position: " + getPosition() + " Salary: " + getSalary();
    }


}
