package com.example.ajay.feeder20;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kanak on 5/11/16.
 */
public class Deadline implements Serializable {
    private String name;
    private int id;
    private String date;
    private String time;
    private int course;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }
}
