package com.example.ajay.feeder20;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kanak on 4/11/16.
 */
public class Course implements Serializable {
    private int id;
    private String Coursename;
    private String Coursecode;
    private String Semester;
    private int Year;
    private List<Feedback> Feed;
    private List<Deadline> Deadline;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCoursename() {
        return Coursename;
    }

    public void setCoursename(String coursename) {
        Coursename = coursename;
    }

    public String getCoursecode() {
        return Coursecode;
    }

    public void setCoursecode(String coursecode) {
        Coursecode = coursecode;
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String semester) {
        Semester = semester;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public List<Feedback> getFeed() {
        return Feed;
    }

    public void setFeed(List<Feedback> feed) {
        Feed = feed;
    }

    public List<Deadline> getDeadline() {
        return Deadline;
    }

    public void setDeadline(List<Deadline> deadline) {
        Deadline = deadline;
    }

}