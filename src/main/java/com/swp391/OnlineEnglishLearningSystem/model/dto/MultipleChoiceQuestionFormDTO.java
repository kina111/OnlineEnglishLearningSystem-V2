package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.AnswerOption;
import com.swp391.OnlineEnglishLearningSystem.model.Question;
import com.swp391.OnlineEnglishLearningSystem.util.AtLeastOneCorrectAnswer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class QuestionFormDTO {
    @NotBlank(message = "Question content is required")
    private String content;

    @NotNull(message = "Media type must be specified (e.g., NONE)")
    private Question.MediaType mediaType;

    private MultipartFile media;

    @AtLeastOneCorrectAnswer(message = "At least one answer option must be marked as correct")
    private List<AnswerOption> answerOptions = new ArrayList<>();

    public QuestionFormDTO() {
        super();
    }

    public QuestionFormDTO(String content, Question.MediaType mediaType, MultipartFile media, List<AnswerOption> answerOptions) {
        this.content = content;
        this.mediaType = mediaType;
        this.media = media;
        this.answerOptions = answerOptions;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Question.MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(Question.MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public MultipartFile getMedia() {
        return media;
    }

    public void setMedia(MultipartFile media) {
        this.media = media;
    }

    public List<AnswerOption> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<AnswerOption> answerOptions) {
        this.answerOptions = answerOptions;
    }
}
