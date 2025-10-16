package com.swp391.OnlineEnglishLearningSystem.util;

import com.swp391.OnlineEnglishLearningSystem.model.Question;
import com.swp391.OnlineEnglishLearningSystem.model.ShortAnswerOption;
import com.swp391.OnlineEnglishLearningSystem.model.dto.MultipleChoiceQuestionFormDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.ShortAnswerQuestionFormDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

// Logic cho annotation @ValidMediaType, áp dụng cho class DTO
public class ValidMediaTypeValidator implements ConstraintValidator<ValidMediaType, Object> { // Dùng Object để có thể tái sử dụng cho các DTO khác

    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "bmp", "webp");
    private static final Set<String> ALLOWED_VIDEO_EXTENSIONS = Set.of("mp4", "avi", "mkv", "mov", "webm");
    private static final Set<String> ALLOWED_AUDIO_EXTENSIONS = Set.of("mp3", "aac", "wav", "ogg");

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        // Lấy các giá trị thuộc tính từ DTO bằng reflection hoặc ép kiểu
        Question.MediaType mediaType = null;
        MultipartFile mediaFile = null;

        // Giả sử DTO là MultipleChoiceQuestionFormDTO
        if (dto instanceof MultipleChoiceQuestionFormDTO) {
            MultipleChoiceQuestionFormDTO questionDTO = (MultipleChoiceQuestionFormDTO) dto;
            mediaType = questionDTO.getMediaType();
            mediaFile = questionDTO.getMedia();
        } else if (dto instanceof ShortAnswerQuestionFormDTO){
            ShortAnswerQuestionFormDTO questionDTO = (ShortAnswerQuestionFormDTO) dto;
            mediaType = questionDTO.getMediaType();
            mediaFile = questionDTO.getMedia();
        }

        // 1. Nếu người dùng chọn NONE, hoặc không upload file, thì luôn hợp lệ
        if ( mediaFile == null ||  mediaType == Question.MediaType.NONE || mediaFile.isEmpty()) {
            return true;
        }

        // 2. Lấy phần mở rộng của file (extension)
        String originalFilename = mediaFile.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return false; // Không có tên file hoặc không có extension -> không hợp lệ
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        // 3. So sánh extension với MediaType đã chọn
        switch (mediaType) {
            case IMAGE:
                return ALLOWED_IMAGE_EXTENSIONS.contains(extension);
            case VIDEO:
                return ALLOWED_VIDEO_EXTENSIONS.contains(extension);
            case AUDIO:
                return ALLOWED_AUDIO_EXTENSIONS.contains(extension);
            default:
                return false; // Trường hợp không xác định
        }
    }
}