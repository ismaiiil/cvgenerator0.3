package com.aequasys.model.vo;

public class Certification {
    private int id;
    private int year;
    private String Qualification;
    private int user_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification( String qualification) {
        this.Qualification = qualification;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }



}
