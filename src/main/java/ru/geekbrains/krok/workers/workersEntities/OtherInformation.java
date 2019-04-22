package ru.geekbrains.krok.workers.workersEntities;

public class OtherInformation {

    int id;
    String phone;
    String address;



    public OtherInformation(int id, String phone, String address) {
        this.id = id;
        this.phone = phone;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
