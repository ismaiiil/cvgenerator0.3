package com.aequasys.model.vo;

public class User {
    private int id;
    private String name;
    private String surname;
    private String position;
    private String email;

//    public User(int id, String name, String surname, String email) {
//        this.id = id;
//        this.name = name;
//        this.surname = surname;
//        this.email = email;
//    }

    public void setId(int id){this.id = id; }

    public int getId(){ return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
