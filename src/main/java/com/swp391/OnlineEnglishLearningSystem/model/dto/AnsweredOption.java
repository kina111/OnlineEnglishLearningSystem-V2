package com.swp391.OnlineEnglishLearningSystem.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AnsweredOption implements Serializable {
    private List<String> answer = new ArrayList<>();
    private boolean bookmarked;

    public AnsweredOption() {}

    public AnsweredOption(List<String> answer, boolean bookmarked) {
        this.setAnswer(answer);
        this.setBookmarked(bookmarked);
    }

    // Getters and Setters
    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = (answer != null) ? answer : new ArrayList<>();
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
