package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class QuizAttemptQuestion {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private QuizAttempt quizAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    private boolean bookmarked = false;
    private int questionOrder;

    // Giữ tên field isCorrect và getter/setter thủ công để tránh xung đột naming convention
    private Boolean isCorrect = false;

    @OneToMany(mappedBy = "attemptQuestion", cascade = CascadeType.ALL)
    private List<QuizAttemptSelectedOption> selectedOptions;

    public QuizAttemptQuestion(QuizAttempt quizAttempt, Question question, boolean bookmarked,
                                int questionOrder, Boolean isCorrect,
                                List<QuizAttemptSelectedOption> selectedOptions) {
        this.quizAttempt = quizAttempt;
        this.question = question;
        this.bookmarked = bookmarked;
        this.questionOrder = questionOrder;
        this.isCorrect = isCorrect;
        this.selectedOptions = selectedOptions;
    }

    // Giữ getter/setter thủ công cho isCorrect vì Lombok sinh getIsCorrect() thay vì getCorrect()
    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }
}
