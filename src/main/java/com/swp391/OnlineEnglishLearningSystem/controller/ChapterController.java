package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.Chapter;
import com.swp391.OnlineEnglishLearningSystem.service.ChapterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    public static class CreateChapterRequest {
            private String name;
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

    @PostMapping("/course/{courseId}")
    public ResponseEntity<ChapterResponse> createChapter(@PathVariable("courseId") Long courseId,
                                                         @RequestBody CreateChapterRequest request
                                                         ){

        Chapter newChapter = chapterService.createChapterForCourse(courseId, request);

        ChapterResponse chapterResponse = new ChapterResponse(newChapter);

        return new ResponseEntity<>(chapterResponse, HttpStatus.CREATED);
    }
}
