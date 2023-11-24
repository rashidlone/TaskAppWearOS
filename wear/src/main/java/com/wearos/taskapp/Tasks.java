package com.wearos.taskapp;

import java.io.Serializable;

public class Tasks implements Serializable {

    private int id;
    private String name;
    private String date;

    public Tasks(int id, String name, String date){
        this.id = id;
        this.date = date;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId(){
        return id;
    }

    public String getDate(){
        return date;
    }


    @Override
    public String toString() {
        return id + "\n" + date + "\n" + name;
    }
}
