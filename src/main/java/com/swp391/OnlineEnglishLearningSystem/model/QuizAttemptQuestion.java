package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;

import java.util.List;

    @Entity
    public class QuizAttemptQuestion {
        @Id @GeneratedValue
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        private QuizAttempt quizAttempt;
        @ManyToOne(fetch = FetchType.LAZY)
        private Question question;

        private boolean bookmarked = false;
        private int questionOrder;
        private Boolean isCorrect = false;

        @OneToMany(mappedBy = "attemptQuestion", cascade = CascadeType.ALL)
        private List<QuizAttemptSelectedOption> selectedOptions;
        public QuizAttemptQuestion() {
        }
        public QuizAttemptQuestion(QuizAttempt quizAttempt, Question question, boolean bookmarked, int questionOrder, Boolean isCorrect, List<QuizAttemptSelectedOption> selectedOptions) {
            this.quizAttempt = quizAttempt;
            this.question = question;
            this.bookmarked = bookmarked;
            this.questionOrder = questionOrder;
            this.isCorrect = isCorrect;
            this.selectedOptions = selectedOptions;
        }
    //getter setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuizAttempt getQuizAttempt() {
        return quizAttempt;
    }

    public void setQuizAttempt(QuizAttempt quizAttempt) {
        this.quizAttempt = quizAttempt;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public int getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(int questionOrder) {
        this.questionOrder = questionOrder;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public List<QuizAttemptSelectedOption> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(List<QuizAttemptSelectedOption> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }
}

