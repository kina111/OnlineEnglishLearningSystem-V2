package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.*;
import com.swp391.OnlineEnglishLearningSystem.model.dto.*;
import com.swp391.OnlineEnglishLearningSystem.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class LessonController {

    private final ChapterService chapterService;
    private final UploadService uploadService;
    private final LessonService lessonService;
    private final QuestionService questionService;
    private final AnswerOptionService answerOptionService;
    private final ShortAnswerOptionService shortAnswerOptionService;

    public LessonController(ChapterService chapterService, UploadService uploadService, LessonService lessonService, QuestionService questionService, AnswerOptionService answerOptionService, ShortAnswerOptionService shortAnswerOptionService) {
        this.chapterService = chapterService;
        this.uploadService = uploadService;
        this.lessonService = lessonService;
        this.questionService = questionService;
        this.answerOptionService = answerOptionService;
        this.shortAnswerOptionService = shortAnswerOptionService;
    }
    //=========================================== Xử lí Lesson ===================================================
    //--- GET LESSONS ---
    @GetMapping("/api/chapters/{chapterId}/lessons")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<LessonResponse>>> getLessons(@PathVariable("chapterId") Long chapterId) {
        Chapter chapter = this.chapterService.findById(chapterId).orElseThrow(() -> new IllegalArgumentException("Chapter not found with id: " + chapterId));
        List<Lesson> lessons = chapter.getLessons();

        List<LessonResponse> lessonResponses = lessons.stream().map(LessonResponse::new).toList();
        ApiResponse<List<LessonResponse>> response = new ApiResponse<>(HttpStatus.OK,
                "List of lessons",
                lessonResponses,
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/lessons/{lessonId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<LessonResponse>> getLesson(@PathVariable("lessonId") Long lessonId) {
        Lesson lesson = this.lessonService.findById(lessonId);
        LessonResponse lessonResponse = new LessonResponse(lesson);

        ApiResponse<LessonResponse> response = new ApiResponse<>(
                HttpStatus.OK,
                "Details of lesson",
                lessonResponse,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //--- CREATE LESSON ---
    @PostMapping("/api/chapters/{chapterId}/lectures")
    @ResponseBody
    public ResponseEntity<ApiResponse<LessonResponse>> createLecture(@PathVariable("chapterId") Long chapterId,
                                                                     @Valid @ModelAttribute CreateLectureRequest request) { // <-- Dùng LectureRequest
        Lesson newLesson = lessonService.createLecture(chapterId, request); // Gọi service tương ứng
        LessonResponse lessonResponse = new LessonResponse(newLesson);
        ApiResponse<LessonResponse> response = new ApiResponse<>(HttpStatus.CREATED, "Tạo bài giảng thành công", lessonResponse, null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/api/chapters/{chapterId}/quizzes")
    @ResponseBody
    public ResponseEntity<ApiResponse<LessonResponse>> createQuiz(@PathVariable("chapterId") Long chapterId,
                                                                  @Valid @ModelAttribute CreateQuizRequest request) { // <-- Dùng LectureRequest
        Lesson newLesson = lessonService.createQuiz(chapterId, request);
        LessonResponse lessonResponse = new LessonResponse(newLesson);
        ApiResponse<LessonResponse> response = new ApiResponse<>(HttpStatus.CREATED, "Tạo bài test thành công", lessonResponse, null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //--- UPDATE LESSON ---
    @PutMapping("/api/lectures/{lectureId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<LessonResponse>> updateLecture(@PathVariable("lectureId") Long lectureId,
                                                                     @Valid @ModelAttribute CreateLectureRequest request){
        Lesson lectureToUpdate = this.lessonService.updateLectureById(lectureId, request);

        LessonResponse lessonResponse = new LessonResponse(lectureToUpdate);
        ApiResponse<LessonResponse> response = new ApiResponse<>(HttpStatus.OK,
                "Lecture updated successfully!",
                lessonResponse,
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/api/quizzes/{quizId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<LessonResponse>> updateLecture(@PathVariable("quizId") Long quizId,
                                                                     @Valid @ModelAttribute CreateQuizRequest request){
        Lesson lectureToUpdate = this.lessonService.updateQuizById(quizId, request);

        LessonResponse lessonResponse = new LessonResponse(lectureToUpdate);
        ApiResponse<LessonResponse> response = new ApiResponse<>(HttpStatus.OK,
                "Quiz updated successfully!",
                lessonResponse,
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //--- DELETE LESSON ---
    @DeleteMapping("/api/chapters/{chapterId}/lessons/{lessonId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> deleteLecture(@PathVariable("chapterId") Long chapterId,
                                                                     @PathVariable("lessonId") Long lessonId){
        this.lessonService.deleteAndReorder(chapterId, lessonId);
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.OK,
                "Lesson deleted successfully!", null, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    //============================== XỬ LÍ QUIZ ===================================
    @GetMapping("/courses/{courseId}/quizzes/{quizId}/questions")
    public String getQuizQuestionBank(@PathVariable("quizId") Long id,
                                      @PathVariable("courseId") Long courseId,
                                      Model model){
        try{
            Lesson quiz = lessonService.findQuizAndQuestions(id);
            model.addAttribute("quiz", quiz);
            model.addAttribute("courseId", courseId);
            return "course/quizQuestions";
        }catch (Exception e){
            return "redirect:/courses/" + courseId;
        }
    }

    @GetMapping("/courses/{courseId}/quizzes/{quizId}/questions/multiple-choice/create")
    public String getMultipleChoiceQuestionPage(@PathVariable("courseId") Long courseId,
                                                @PathVariable("quizId") Long quizId,
                                                Model model){
        Lesson quiz = lessonService.findById(quizId);
        MultipleChoiceQuestionFormDTO multipleChoiceQuestionFormDTO = new MultipleChoiceQuestionFormDTO();

        multipleChoiceQuestionFormDTO.getAnswerOptions().add(new AnswerOption());
        multipleChoiceQuestionFormDTO.getAnswerOptions().add(new AnswerOption());

        model.addAttribute("isUpdate", false);
        model.addAttribute("mediaTypes", Question.MediaType.values());
        model.addAttribute("quiz", quiz);
        model.addAttribute("courseId", courseId);
        model.addAttribute("multipleChoiceQuestionFormDTO", multipleChoiceQuestionFormDTO);

        return "course/createMultipleChoiceQuestion";
    }

    @PostMapping("/courses/{courseId}/quizzes/{quizId}/questions/multiple-choice/create")
    public String createQuizQuestion(@PathVariable("courseId") Long courseId,
                                     @PathVariable("quizId") Long quizId,
                                     @Valid @ModelAttribute("multipleChoiceQuestionFormDTO") MultipleChoiceQuestionFormDTO multipleChoiceQuestionFormDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model){
        if (bindingResult.hasErrors()) {
            Lesson quiz = lessonService.findById(quizId);
            if (quiz == null) {
                throw new IllegalArgumentException("Không tìm thấy Quiz với ID: " + quizId);
            }
            model.addAttribute("isUpdate", false);
            model.addAttribute("courseId", courseId);
            model.addAttribute("quiz", quiz); // 2. Add quiz back to the model
            model.addAttribute("mediaTypes", Question.MediaType.values());// 3. Add mediaTypes back too

            return "course/createMultipleChoiceQuestion";
        }
        try{
            //save new question
            Question newQuestion = new Question();
            newQuestion.setQuestionType(Question.QuestionType.MULTIPLE_CHOICE);
            newQuestion.setLesson(lessonService.findById(quizId));
            newQuestion.setMediaType(multipleChoiceQuestionFormDTO.getMediaType());

            newQuestion.setContent(multipleChoiceQuestionFormDTO.getContent());
            if (multipleChoiceQuestionFormDTO.getMedia() != null && !multipleChoiceQuestionFormDTO.getMedia().isEmpty()){
                String fileName = this.uploadService.uploadFile(multipleChoiceQuestionFormDTO.getMedia(), "quizzes/media", null);
                newQuestion.setMediaUrl(fileName);
            }else{
                newQuestion.setMediaType(Question.MediaType.NONE);
            }
            this.questionService.save(newQuestion);

            //save AnswerOption
            List<AnswerOption> answerOptions = multipleChoiceQuestionFormDTO.getAnswerOptions();
            answerOptions.forEach(answerOption -> {
               answerOption.setQuestion(newQuestion);
               this.answerOptionService.save(answerOption);
            });

            redirectAttributes.addFlashAttribute("message", "Question created successfully");
            return "redirect:/courses/" + courseId + "/quizzes/" + quizId + "/questions";
        }catch (Exception e){
            bindingResult.reject("global.error", e.getMessage());
            return "redirect:/courses/" + courseId + "/quizzes/" + quizId + "/questions";
        }
    }

    @GetMapping("/courses/{courseId}/quizzes/{quizId}/questions/short-answer/create")
    public String getShortAnswerQuestionPage(@PathVariable("courseId") Long courseId,
                                             @PathVariable("quizId") Long quizId,
                                             Model model){
        Lesson quiz = lessonService.findById(quizId);
        ShortAnswerQuestionFormDTO newQuestion = new ShortAnswerQuestionFormDTO();

        model.addAttribute("isUpdate", false);
        model.addAttribute("courseId", courseId);
        model.addAttribute("mediaTypes", Question.MediaType.values());
        model.addAttribute("quiz", quiz);
        model.addAttribute("shortAnswerQuestionFormDTO", newQuestion);

        return "course/createShortAnswerQuestion";
    }

    @PostMapping("/courses/{courseId}/quizzes/{quizId}/questions/short-answer/create")
    public String createQuizQuestion(@PathVariable("courseId") Long courseId,
                                     @PathVariable("quizId") Long quizId,
                                     @Valid @ModelAttribute("shortAnswerQuestionFormDTO") ShortAnswerQuestionFormDTO shortAnswerQuestionFormDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model){
        if (bindingResult.hasErrors()) {
            Lesson quiz = lessonService.findById(quizId);
            if (quiz == null) {
                throw new IllegalArgumentException("Không tìm thấy Quiz với ID: " + quizId);
            }
            model.addAttribute("isUpdate", false);
            model.addAttribute("courseId", courseId);
            model.addAttribute("quiz", quiz); // 2. Add quiz back to the model
            model.addAttribute("mediaTypes", Question.MediaType.values());// 3. Add mediaTypes back too

            return "course/createShortAnswerQuestion";
        }
        try{
            //save new question
            Question newQuestion = new Question();
            newQuestion.setQuestionType(Question.QuestionType.SHORT_ANSWER);
            newQuestion.setLesson(lessonService.findById(quizId));
            newQuestion.setMediaType(shortAnswerQuestionFormDTO.getMediaType());
            newQuestion.setContent(shortAnswerQuestionFormDTO.getContent());

            if (shortAnswerQuestionFormDTO.getMedia() != null && !shortAnswerQuestionFormDTO.getMedia().isEmpty()){
                String fileName = this.uploadService.uploadImage(shortAnswerQuestionFormDTO.getMedia(), "quizzes/media");
                newQuestion.setMediaUrl(fileName);
            }else{
                newQuestion.setMediaType(Question.MediaType.NONE);
            }
            this.questionService.save(newQuestion);

            //save short answer option
            ShortAnswerOption answerOption = new ShortAnswerOption();
            answerOption.setQuestion(newQuestion);
            answerOption.setSolutionText(shortAnswerQuestionFormDTO.getSolutionText());
            this.shortAnswerOptionService.save(answerOption);

            redirectAttributes.addFlashAttribute("message", "Question created successfully");
            return "redirect:/courses/" + courseId + "/quizzes/" + quizId + "/questions";
        }catch (Exception e){
            bindingResult.reject("global.error", e.getMessage());
            return "redirect:/courses/" + courseId + "/quizzes/" + quizId + "/questions";
        }
    }

    //===== Handle delete question =====
    @PostMapping("/courses/{courseId}/quizzes/{quizId}/questions/{questionId}/delete")
    public String deleteMultipleChoiceQuestion(@PathVariable("questionId") Long questionId,
                                               @PathVariable("courseId") Long courseId,
                                               @PathVariable("quizId") Long quizId,
                                               RedirectAttributes redirectAttributes,
                                               Model model){
        try{
            Question question = questionService.findById(questionId);
            questionService.delete(question);
            redirectAttributes.addFlashAttribute("message", "Question deleted successfully");
            return "redirect:/courses/" + courseId + "/quizzes/" + quizId + "/questions";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/courses/" + courseId + "/quizzes/" + quizId + "/questions";
    }


    //==================== UPDATE MULTIPLE CHOICE QUESTION ====================

    @GetMapping("/courses/{courseId}/quizzes/{quizId}/questions/{questionId}/multiple-choice/update")
    public String getUpdateMultipleChoiceQuestionPage(@PathVariable("questionId") Long questionId,
                                                      @PathVariable("courseId") Long courseId,
                                                      @PathVariable("quizId") Long quizId,
                                                      Model model) {
        try {
            Question question = questionService.findByIdWithAnswerOptions(questionId);
            Lesson quiz = lessonService.findById(quizId);

            // Map từ Entity sang DTO để đổ dữ liệu vào form
            MultipleChoiceQuestionFormDTO dto = new MultipleChoiceQuestionFormDTO();
            dto.setContent(question.getContent());
            dto.setMediaType(question.getMediaType());
            // Lấy danh sách câu trả lời đã có
            List<AnswerOption> existingOptions = question.getAnswerOptions();
            dto.setAnswerOptions(existingOptions);

            model.addAttribute("isUpdate", true); // Đánh dấu đây là form update
            model.addAttribute("pageTitle", "Cập nhật câu hỏi trắc nghiệm");
            model.addAttribute("quiz", quiz);
            model.addAttribute("courseId", courseId);
            model.addAttribute("questionId", questionId); // Truyền questionId để form action biết
            model.addAttribute("multipleChoiceQuestionFormDTO", dto);
            model.addAttribute("mediaTypes", Question.MediaType.values());

            return "course/createMultipleChoiceQuestion"; // Tái sử dụng view create
        } catch (Exception e) {
            // Handle error, maybe redirect with an error message
            return "redirect:/courses/" + courseId + "/quizzes/" + quizId + "/questions";
        }
    }

    @PostMapping("/courses/{courseId}/quizzes/{quizId}/questions/{questionId}/multiple-choice/update")
    public String updateMultipleChoiceQuestion(@PathVariable("questionId") Long questionId,
                                               @PathVariable("courseId") Long courseId,
                                               @PathVariable("quizId") Long quizId,
                                               @Valid @ModelAttribute("multipleChoiceQuestionFormDTO") MultipleChoiceQuestionFormDTO dto,
                                               BindingResult bindingResult,
                                               RedirectAttributes redirectAttributes,
                                               Model model) {
        if (bindingResult.hasErrors()) {
            Lesson quiz = lessonService.findById(quizId);
            model.addAttribute("isUpdate", true);
            model.addAttribute("pageTitle", "Cập nhật câu hỏi trắc nghiệm");
            model.addAttribute("quiz", quiz);
            model.addAttribute("courseId", courseId);
            model.addAttribute("questionId", questionId);
            model.addAttribute("mediaTypes", Question.MediaType.values());
            return "course/createMultipleChoiceQuestion";
        }

        try {
            Question questionToUpdate = questionService.findById(questionId);
            questionToUpdate.setContent(dto.getContent());
            questionToUpdate.setMediaType(dto.getMediaType());

            // Xử lý upload file mới (nếu có)
            if (dto.getMedia() != null && !dto.getMedia().isEmpty()) {
                String fileName = this.uploadService.uploadFile(dto.getMedia(), "quizzes/media", null);
                questionToUpdate.setMediaUrl(fileName);
            } else if (dto.getMediaType() == Question.MediaType.NONE) {
                questionToUpdate.setMediaUrl(null); // Xóa media nếu người dùng chọn NONE
            }

            questionService.save(questionToUpdate);

            //xóa và thêm mới các lựa chọn
            //khi xóa ở question, các answerOption đó sẽ bị mồ côi và cũng bị xóa (do orphanRemoval = true)
            questionToUpdate.getAnswerOptions().clear();

            dto.getAnswerOptions().forEach(option -> {
                option.setQuestion(questionToUpdate);
                answerOptionService.save(option);
            });

            redirectAttributes.addFlashAttribute("message", "Cập nhật câu hỏi thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật câu hỏi: " + e.getMessage());
        }

        return "redirect:/courses/" + courseId + "/quizzes/" + quizId + "/questions";
    }

    //==================== UPDATE SHORT ANSWER QUESTION ====================

    @GetMapping("/courses/{courseId}/quizzes/{quizId}/questions/{questionId}/short-answer/update")
    public String getUpdateShortAnswerQuestionPage(@PathVariable("questionId") Long questionId,
                                                   @PathVariable("courseId") Long courseId,
                                                   @PathVariable("quizId") Long quizId,
                                                   Model model) {
        try {
            Question question = questionService.findById(questionId);
            Lesson quiz = lessonService.findById(quizId);
            ShortAnswerOption solution = question.getShortAnswerOption();

            ShortAnswerQuestionFormDTO dto = new ShortAnswerQuestionFormDTO();
            dto.setContent(question.getContent());
            dto.setMediaType(question.getMediaType());
            dto.setSolutionText(solution.getSolutionText());

            model.addAttribute("isUpdate", true);
            model.addAttribute("pageTitle", "Cập nhật câu hỏi trả lời ngắn");
            model.addAttribute("quiz", quiz);
            model.addAttribute("courseId", courseId);
            model.addAttribute("questionId", questionId);
            model.addAttribute("shortAnswerQuestionFormDTO", dto);
            model.addAttribute("mediaTypes", Question.MediaType.values());

            return "course/createShortAnswerQuestion"; // Tái sử dụng view create
        } catch (Exception e) {
            return "redirect:/courses/" + courseId + "/quizzes/" + quizId + "/questions";
        }
    }

    @PostMapping("/courses/{courseId}/quizzes/{quizId}/questions/{questionId}/short-answer/update")
    public String updateShortAnswerQuestion(@PathVariable("questionId") Long questionId,
                                            @PathVariable("courseId") Long courseId,
                                            @PathVariable("quizId") Long quizId,
                                            @Valid @ModelAttribute("shortAnswerQuestionFormDTO") ShortAnswerQuestionFormDTO dto,
                                            BindingResult bindingResult,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {
        if (bindingResult.hasErrors()) {
            Lesson quiz = lessonService.findById(quizId);
            model.addAttribute("isUpdate", true);
            model.addAttribute("pageTitle", "Cập nhật câu hỏi trả lời ngắn");
            model.addAttribute("quiz", quiz);
            model.addAttribute("courseId", courseId);
            model.addAttribute("questionId", questionId);
            model.addAttribute("mediaTypes", Question.MediaType.values());
            return "course/createShortAnswerQuestion";
        }

        try {
            Question questionToUpdate = questionService.findById(questionId);
            questionToUpdate.setContent(dto.getContent());
            questionToUpdate.setMediaType(dto.getMediaType());

            if (dto.getMedia() != null && !dto.getMedia().isEmpty()) {
                String fileName = this.uploadService.uploadFile(dto.getMedia(), "quizzes/media", null);
                questionToUpdate.setMediaUrl(fileName);
            } else if (dto.getMediaType() == Question.MediaType.NONE) {
                questionToUpdate.setMediaUrl(null);
            }

            questionService.save(questionToUpdate);

            ShortAnswerOption solution = questionToUpdate.getShortAnswerOption();
            solution.setSolutionText(dto.getSolutionText());
            shortAnswerOptionService.save(solution);

            redirectAttributes.addFlashAttribute("message", "Cập nhật câu hỏi thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật câu hỏi: " + e.getMessage());
        }

        return "redirect:/courses/" + courseId + "/quizzes/" + quizId + "/questions";
    }
}
