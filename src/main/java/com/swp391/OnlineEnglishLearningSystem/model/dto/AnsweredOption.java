package com.swp391.OnlineEnglishLearningSystem.model.dto;

import java.io.Serializable;

public class AnsweredOption implements Serializable {
    private String answer;
    private boolean bookmarked;

    public AnsweredOption() {}

    public AnsweredOption(String answer, boolean bookmarked) {
        this.answer = answer;
        this.bookmarked = bookmarked;
    }

    // Getters and Setters
    public String getAnswerId() {
        return answer;
    }

    public void setAnswerId(String answerId) {
        this.answer = answerId;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    @Override
    public String toString() {
        return "AnsweredOption{" +
                "answer='" + answer + '\'' +
                ", bookmarked=" + bookmarked +
                '}';
    }
}
