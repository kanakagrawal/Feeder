package com.example.ajay.feeder20;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kanak on 4/11/16.
 */

public class Feedback implements Serializable {
    private String feedback_name;
    private int feed_id;
    private String feed_deadline_date;
    private String feed_deadline_time;
    private String feed_course;
    private List<RatingQuestion> rateque;
    private List<SubjectiveQuestion> subque;

    public List<RatingQuestion> getRateque() {
        return rateque;
    }

    public void setRateque(List<RatingQuestion> rateque) {
        this.rateque = rateque;
    }

    public List<SubjectiveQuestion> getSubque() {
        return subque;
    }

    public void setSubque(List<SubjectiveQuestion> subque) {
        this.subque = subque;
    }

    public String getFeedback_name() {
        return feedback_name;
    }

    public void setFeedback_name(String feedback_name) {
        this.feedback_name = feedback_name;
    }

    public int getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(int feed_id) {
        this.feed_id = feed_id;
    }

    public String getFeed_deadline_date() {
        return feed_deadline_date;
    }

    public void setFeed_deadline_date(String feed_deadline_date) {
        this.feed_deadline_date = feed_deadline_date;
    }

    public String getFeed_deadline_time() {
        return feed_deadline_time;
    }

    public void setFeed_deadline_time(String feed_deadline_time) {
        this.feed_deadline_time = feed_deadline_time;
    }

    public String getFeed_course() {
        return feed_course;
    }

    public void setFeed_course(String feed_course) {
        this.feed_course = feed_course;
    }
}