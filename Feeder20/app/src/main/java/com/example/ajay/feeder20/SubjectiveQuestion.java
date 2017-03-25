package com.example.ajay.feeder20;

import java.io.Serializable;

/**
 * Created by kanak on 5/11/16.
 */
public class SubjectiveQuestion implements Serializable {
    private int id;
    private String question;
    private int feedback_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(int feedback_id) {
        this.feedback_id = feedback_id;
    }
}
