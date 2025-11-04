package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.AnswerOption;
import com.swp391.OnlineEnglishLearningSystem.model.Question;
import com.swp391.OnlineEnglishLearningSystem.util.AtLeastOneCorrectAnswer;
import com.swp391.OnlineEnglishLearningSystem.util.ValidMediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@ValidMediaType
public class MultipleChoiceQuestionFormDTO {
    @NotBlank(message = "Vui lòng nhập nội dung câu hỏi")
    private String content;

    @NotNull(message = "Vui lòng chọn loại media kèm theo")
    private Question.MediaType mediaType;

    private MultipartFile media;

    @AtLeastOneCorrectAnswer(message = "Câu hỏi cần có ít nhất 1 đáp án đúng")
    private List<AnswerOption> answerOptions = new ArrayList<>();

    public MultipleChoiceQuestionFormDTO() {
        super();
    }

    public MultipleChoiceQuestionFormDTO(String content, Question.MediaType mediaType, MultipartFile media, List<AnswerOption> answerOptions) {
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
