package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.ApiResponse;
import com.swp391.OnlineEnglishLearningSystem.model.Chapter;
import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.service.ChapterService;
import com.swp391.OnlineEnglishLearningSystem.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;
    private final CourseService courseService;

    public static class CreateChapterRequest {
        @NotBlank(message = "Vui lòng nhập tên chương")
        @Size(min = 5, max = 100, message = "Độ dài tên chương phải từ 5-100 kí tự")
        private String name;

        @NotBlank(message = "Vui lòng nhập mô tả")
        @Size(min = 10, max = 200, message = "Độ dài mô tả phải từ 10-200 kí tự")
        private String shortDescription;

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

       public String getShortDescription() {
           return shortDescription;
       }

       public void setShortDescription(String shortDescription) {
           this.shortDescription = shortDescription;
       }
   }

    public static class ChapterResponse {
       private Long id;
       private String name;
       private String shortDescription;
       private int orderNumber;

       public ChapterResponse(Chapter chapter){
           this.id = chapter.getId();
           this.name = chapter.getName();
           this.shortDescription = chapter.getShortDescription();
           this.orderNumber = chapter.getOrderNumber();
       }

       public Long getId() {
           return id;
       }

       public void setId(Long id) {
           this.id = id;
       }

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

       public String getshortDescription() {
           return shortDescription;
       }

       public void setShortDescription(String shortDescription) {
           this.shortDescription = shortDescription;
       }

       public int getOrderNumber() {
           return orderNumber;
       }

       public void setOrderNumber(int orderNumber) {
           this.orderNumber = orderNumber;
       }
   }

    @PostMapping("/api/courses/{courseId}/chapters")
    public ResponseEntity<ApiResponse<ChapterResponse>> createChapter(@PathVariable("courseId") Long courseId,
                                                     @Valid @RequestBody CreateChapterRequest request
                                                         ){

        Chapter newChapter = chapterService.createChapterForCourse(courseId, request);

        ChapterResponse chapterResponse = new ChapterResponse(newChapter);

        ApiResponse<ChapterResponse> response = new ApiResponse<>(HttpStatus.CREATED,
                "Chapter created successfully",
                chapterResponse,
                "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/api/courses/{courseId}/chapters")
    public ResponseEntity<ApiResponse<List<ChapterResponse>>> getAllChapters(@PathVariable("courseId") Long courseId) {
        Course course = this.courseService.findById(courseId);
        List<Chapter> chapters = course.getChapters();
        List<ChapterResponse> chapterResponses = chapters.stream()
                .map(ChapterResponse::new)
                .toList();

        ApiResponse<List<ChapterResponse>> response = new ApiResponse<>(HttpStatus.OK,
                "List of chapters",
                chapterResponses,
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/chapters/{chapterId}")
    public ResponseEntity<ApiResponse<ChapterResponse>> getChapterById(@PathVariable("chapterId") Long chapterId) {
        ChapterResponse chapterResponse = this.chapterService.findById(chapterId)
                .map(ChapterResponse::new)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        ApiResponse<ChapterResponse> response = new ApiResponse<>(HttpStatus.OK,
                "Chapter detail",
                chapterResponse,
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/api/chapters/{chapterId}")
    public ResponseEntity<ApiResponse<ChapterResponse>> updateChapterById(@PathVariable("chapterId") Long chapterId,
                                                                          @Valid @RequestBody CreateChapterRequest chapter) {
        Chapter updateChapter = this.chapterService.updateChapter(chapterId, chapter);

        ChapterResponse chapterResponse = new ChapterResponse(updateChapter);
        ApiResponse<ChapterResponse> response = new ApiResponse<>(HttpStatus.OK,
                "Chapter detail",
                chapterResponse,
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/api/courses/{courseId}/chapters/{chapterId}")
    public ResponseEntity<ApiResponse<Void>> deleteChapterById(@PathVariable("courseId") Long courseId,
                                                               @PathVariable("chapterId") Long chapterId) {
        try{
            this.chapterService.deleteChapterAndReorder(courseId, chapterId);
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.OK, "Xóa chương thành công", null, null);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.BAD_REQUEST,
                    "Không thể xóa chương học!", null, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
