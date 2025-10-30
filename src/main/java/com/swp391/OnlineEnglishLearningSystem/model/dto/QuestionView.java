package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Question;

public class QuestionView {
    private Question question;
    private boolean answered;
    private boolean bookmarked;

    public QuestionView() {}


    public QuestionView(Question question, boolean answered, boolean bookmarked) {
        this.question = question;
        this.answered = answered;
        this.bookmarked = bookmarked;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
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
