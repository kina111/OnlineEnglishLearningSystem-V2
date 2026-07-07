package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Question;
import com.swp391.OnlineEnglishLearningSystem.util.ValidMediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@ValidMediaType
public class ShortAnswerQuestionFormDTO {
    @NotBlank(message = "Vui lòng nhập nội dung câu hỏi")
    private String content;

    @NotNull(message = "Vui lòng chọn loại media kèm theo")
    private Question.MediaType mediaType;

    private MultipartFile media;

    @NotBlank(message = "Vui lòng nhập đáp án cho câu hỏi")
    private String solutionText;

    public ShortAnswerQuestionFormDTO(String content, Question.MediaType mediaType,
                                       MultipartFile media, String solutionText) {
        this.content = content;
        this.mediaType = mediaType;
        this.media = media;
        this.solutionText = solutionText;
    }
}
