package com.example.ajay.feeder20;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ajay on 4/11/16.
 */
public class Student implements Serializable {
    private int Id;
    private String Name;
    private String Email_id;
    private String Password;
    private String Rollnumber;
    private List<Course> Subjects;
    private List<RatingAnswer> RAnswers;
    private List<SubjectiveAnswer> SAnswers;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail_id() {
        return Email_id;
    }

    public void setEmail_id(String email_id) {
        Email_id = email_id;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRollnumber() {
        return Rollnumber;
    }

    public void setRollnumber(String rollnumber) {
        Rollnumber = rollnumber;
    }

    public List<Course> getSubjects() {
        return Subjects;
    }

    public void setSubjects(List<Course> subjects) {
        Subjects = subjects;
    }

    public List<RatingAnswer> getRAnswers() {
        return RAnswers;
    }

    public void setRAnswers(List<RatingAnswer> RAnswers) {
        this.RAnswers = RAnswers;
    }

    public List<SubjectiveAnswer> getSAnswers() {
        return SAnswers;
    }

    public void setSAnswers(List<SubjectiveAnswer> SAnswers) {
        this.SAnswers = SAnswers;
    }
}
