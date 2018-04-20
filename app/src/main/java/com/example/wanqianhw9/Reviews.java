package com.example.wanqianhw9;

/**
 * Created by anwanqi on 4/19/18.
 */

public class Reviews {
    private String profileUrl;
    private String author;
    private String authorUrl;
    private double rate;
    private String description;
    private String review_time;

    public Reviews(String profileUrl, String author, String authorUrl, double rate, String description, String review_time) {
        this.profileUrl = profileUrl;
        this.author = author;
        this.authorUrl = authorUrl;
        this.rate = rate;
        this.description = description;
        this.review_time = review_time;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReview_time() {
        return review_time;
    }

    public void setReview_time(String review_time) {
        this.review_time = review_time;
    }
}
