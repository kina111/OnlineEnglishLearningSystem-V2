package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionView {
    private Question question;
    private boolean answered;
    private boolean bookmarked;

    public QuestionView(Question question, boolean answered, boolean bookmarked) {
        this.question = question;
        this.answered = answered;
        this.bookmarked = bookmarked;
    }

    @Override
    public String toString() {
        return "QuestionView{" +
                "question=" + question +
                ", answered=" + answered +
                ", bookmarked=" + bookmarked +
                '}';
    }
}
