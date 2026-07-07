package com.swp391.OnlineEnglishLearningSystem.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class AnsweredOption implements Serializable {
    private List<String> answer = new ArrayList<>();
    private boolean bookmarked;

    public AnsweredOption(List<String> answer, boolean bookmarked) {
        this.setAnswer(answer);
        this.setBookmarked(bookmarked);
    }

    // Giữ setter tùy chỉnh: có logic null-check
    public void setAnswer(List<String> answer) {
        this.answer = (answer != null) ? answer : new ArrayList<>();
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
