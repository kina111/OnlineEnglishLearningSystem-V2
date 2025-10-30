package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.AnswerOption;
import com.swp391.OnlineEnglishLearningSystem.model.Question;
import com.swp391.OnlineEnglishLearningSystem.model.ShortAnswerOption;
import com.swp391.OnlineEnglishLearningSystem.util.AtLeastOneCorrectAnswer;
import com.swp391.OnlineEnglishLearningSystem.util.ValidMediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@ValidMediaType
public class ShortAnswerQuestionFormDTO {
    @NotBlank(message = "Vui lòng nhập nội dung câu hỏi")
    private String content;

    @NotNull(message = "Vui lòng chọn loại media kèm theo")
    private Question.MediaType mediaType;

    private MultipartFile media;

    @NotBlank(message = "Vui lòng nhập đáp án cho câu hỏi")
    private String solutionText;

    public ShortAnswerQuestionFormDTO() {
        super();
    }

    public ShortAnswerQuestionFormDTO(String content, Question.MediaType mediaType, MultipartFile media, String solutionText) {
        this.content = content;
        this.mediaType = mediaType;
        this.media = media;
        this.solutionText = solutionText;
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

    public String getSolutionText() {
        return solutionText;
    }

    public void setSolutionText(String solutionText) {
        this.solutionText = solutionText;
    }
}
