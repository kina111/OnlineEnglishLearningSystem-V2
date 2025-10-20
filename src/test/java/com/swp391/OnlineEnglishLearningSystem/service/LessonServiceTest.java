package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Chapter;
import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CreateLectureRequest;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CreateQuizRequest;
import com.swp391.OnlineEnglishLearningSystem.repository.ChapterRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.LessonRepository;
import com.swp391.OnlineEnglishLearningSystem.service.impl.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //dùng mockito
public class LessonServiceTest {
    @Mock
    private ChapterRepository chapterRepository;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private UploadService uploadService;
    @Mock
    private MultipartFile mockVideoFile;

    @InjectMocks
    private LessonServiceImpl lessonService;

    //biến dùng chung
    private Chapter chapter;
    private CreateLectureRequest lectureRequest;
    private CreateQuizRequest quizRequest;
    private final Long chapterId = 1L;

    //hàm chạy trước mỗi test để chuẩn bị dữ liệu
    @BeforeEach
    void setUp(){
        chapter = new Chapter();
        chapter.setId(chapterId);
        chapter.setName("Test Chapter");
        chapter.setLessons(new ArrayList<>());

        lectureRequest = new CreateLectureRequest();
        lectureRequest.setTitle("Test Lecture");
        lectureRequest.setEstimatedTime(10);
        lectureRequest.setHtmlContent("<p>Test HTML content</p>");
        lectureRequest.setVideo(mockVideoFile);

        quizRequest = new CreateQuizRequest();
        quizRequest.setTitle("Test Quiz");
        quizRequest.setNumberOfQuestions(15);
        quizRequest.setPassRate(80);
        quizRequest.setTimeLimitInMinutes(10);
    }

    @Test
    void createLecture_whenValidRequest_shouldReturnNewLesson(){
        //arrange
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(mockVideoFile.isEmpty()).thenReturn(false);
        when(uploadService.uploadVideo(any(MultipartFile.class), anyString())).thenReturn("lectures/video/mock.mp4");
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //act
        Lesson actualLesson = lessonService.createLecture(chapterId, lectureRequest);

        //assert
        assertNotNull(actualLesson);
        assertEquals(lectureRequest.getTitle(), actualLesson.getTitle());
        assertEquals(lectureRequest.getEstimatedTime(), actualLesson.getEstimatedTime());
        assertEquals(lectureRequest.getHtmlContent(), actualLesson.getHtmlContent());
        assertEquals("lectures/video/mock.mp4", actualLesson.getVideoUrl());
        assertEquals(chapter, actualLesson.getChapter());
        assertEquals(Lesson.LessonType.LECTURE, actualLesson.getLessonType());
        assertEquals(chapter.getLessons().size() + 1, actualLesson.getOrderNumber());

        verify(chapterRepository, times(1)).findById(chapterId);
        verify(uploadService, times(1)).uploadVideo(mockVideoFile, "lectures/videos");
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void createLecture_whenInvalidChapterId_shouldThrowException(){
        //arrange
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.empty());

        //act & assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> lessonService.createLecture(chapterId, lectureRequest));
        assertEquals("Chapter not found", exception.getMessage());

        verify(chapterRepository, times(1)).findById(chapterId);
        verifyNoInteractions(uploadService, lessonRepository);
    }

    @Test
    void createLecture_whenVideoFileIsNull_shouldThrowException(){
        //arrange
        lectureRequest.setVideo(null);
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));

        //act & assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> lessonService.createLecture(chapterId, lectureRequest));
        assertEquals("Video file is empty", exception.getMessage());

        verify(chapterRepository, times(1)).findById(chapterId);
        verifyNoInteractions(uploadService, lessonRepository);
    }

    @Test
    void createLecture_whenVideoFileIsEmpty_shouldThrowException(){
        //arrange
        lectureRequest.setVideo(mockVideoFile);
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(mockVideoFile.isEmpty()).thenReturn(true);

        //act & assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> lessonService.createLecture(chapterId, lectureRequest));
        assertEquals("Video file is empty", exception.getMessage());

        verify(chapterRepository, times(1)).findById(chapterId);
        verifyNoInteractions(uploadService, lessonRepository);
    }

    @Test
    void createQuiz_whenInvalidChapterId_shouldThrowException(){
        //arrange
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.empty());

        //act & assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> lessonService.createQuiz(chapterId, quizRequest));
        assertEquals("Chapter not found", exception.getMessage());

        verify(chapterRepository, times(1)).findById(chapterId);
        verifyNoInteractions(uploadService, lessonRepository);
    }

    @Test
    void createQuiz_whenValidRequest_shouldReturnLesson(){
        //arrange
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //act & assert
        Lesson actualQuiz = lessonService.createQuiz(chapterId, quizRequest);
        assertNotNull(actualQuiz);
        assertEquals(quizRequest.getTitle(), actualQuiz.getTitle());
        assertEquals(quizRequest.getNumberOfQuestions(), actualQuiz.getNumberOfQuestions());
        assertEquals(quizRequest.getPassRate(), actualQuiz.getPassRate());
        assertEquals(quizRequest.getTimeLimitInMinutes(), actualQuiz.getTimeLimitInMinutes());
        assertEquals(chapter, actualQuiz.getChapter());
        assertEquals(Lesson.LessonType.QUIZ, actualQuiz.getLessonType());
        assertEquals(chapter.getLessons().size() + 1, actualQuiz.getOrderNumber());

        verify(chapterRepository, times(1)).findById(chapterId);
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void deleteAndReorder_whenInvaLidChapterId_shouldThrowException(){
        //arrange
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.empty());

        //act & assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> lessonService.deleteAndReorder(chapterId, 1L));
        assertEquals("Chapter not found", exception.getMessage());
        verify(chapterRepository, times(1)).findById(chapterId);
        verifyNoInteractions(lessonRepository);
    }

    @Test
    void deleteAndReorder_whenInvalidLessonId_shouldThrowException(){
        //arrange
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        //act & assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> lessonService.deleteAndReorder(chapterId, 1L));
        assertEquals("Lesson not found", exception.getMessage());

        verify(chapterRepository, times(1)).findById(chapterId);
        verify(lessonRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(lessonRepository);
    }

    @Test
    void deleteAndReorder_whenLessonNotBelongToChapter_shouldThrowException(){
        //arrange
        Chapter differentChapter = new Chapter();
        differentChapter.setId(99L);
        Lesson lessonToDelete = new Lesson();
        lessonToDelete.setId(1L);
        lessonToDelete.setChapter(differentChapter);

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lessonToDelete));

        //act & assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> lessonService.deleteAndReorder(chapterId, 1L));
        assertEquals("Lesson does not belong to the chapter", exception.getMessage());

        verify(chapterRepository, times(1)).findById(chapterId);
        verify(lessonRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(lessonRepository);
    }

    @Test
    void deleteAndReorder_whenValidAndDeleteAtMid_shouldDeleteLesson(){
        //arrange
        Lesson lesson1 = new Lesson(); lesson1.setId(1L); lesson1.setOrderNumber(1); lesson1.setChapter(chapter);
        Lesson lesson3 = new Lesson(); lesson3.setId(3L); lesson3.setOrderNumber(3); lesson3.setChapter(chapter);
        Long lessonIdToDelete = 2L;
        Lesson lessonToDelete = new Lesson(); lessonToDelete.setId(lessonIdToDelete); lessonToDelete.setOrderNumber(2);lessonToDelete.setChapter(chapter);
        List<Lesson> remainingLessons = new ArrayList<>(List.of(lesson1, lesson3));


        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(lessonRepository.findById(lessonIdToDelete)).thenReturn(Optional.of(lessonToDelete));
        when(lessonRepository.findByChapterIdOrderByOrderNumberAsc(chapterId)).thenReturn(remainingLessons);
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //act & assert
        this.lessonService.deleteAndReorder(chapterId, lessonIdToDelete);

        verify(chapterRepository, times(1)).findById(chapterId);
        verify(lessonRepository, times(1)).findById(lessonIdToDelete);
        verify(lessonRepository, times(1)).findByChapterIdOrderByOrderNumberAsc(chapterId);

        ArgumentCaptor<Lesson> lessonCaptor = ArgumentCaptor.forClass(Lesson.class);
        // Expecting 2 saves (one for lesson1, one for lesson3)
        verify(lessonRepository, times(2)).save(lessonCaptor.capture());
        List<Lesson> savedLessons = lessonCaptor.getAllValues();
        Lesson savedLesson1 = savedLessons.stream().filter(l -> l.getId() == 1L).findFirst().orElse(null);
        Lesson savedLesson3 = savedLessons.stream().filter(l -> l.getId() == 3L).findFirst().orElse(null);

        assertEquals(2, savedLessons.size());
        assertEquals(1, savedLesson1.getOrderNumber());
        assertEquals(2, savedLesson3.getOrderNumber());
    }

    @Test
    void deleteAndReorder_whenValidAndDeleteAtBegin_shouldDeleteLesson(){
        //arrange
        Lesson lesson2 = new Lesson(); lesson2.setId(2L); lesson2.setOrderNumber(2); lesson2.setChapter(chapter);
        Lesson lesson3 = new Lesson(); lesson3.setId(3L); lesson3.setOrderNumber(3); lesson3.setChapter(chapter);
        Long lessonIdToDelete = 1L;
        Lesson lessonToDelete = new Lesson(); lessonToDelete.setId(lessonIdToDelete); lessonToDelete.setOrderNumber(1);lessonToDelete.setChapter(chapter);
        List<Lesson> remainingLessons = new ArrayList<>(List.of(lesson2, lesson3));


        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(lessonRepository.findById(lessonIdToDelete)).thenReturn(Optional.of(lessonToDelete));
        when(lessonRepository.findByChapterIdOrderByOrderNumberAsc(chapterId)).thenReturn(remainingLessons);
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //act & assert
        this.lessonService.deleteAndReorder(chapterId, lessonIdToDelete);

        verify(chapterRepository, times(1)).findById(chapterId);
        verify(lessonRepository, times(1)).findById(lessonIdToDelete);
        verify(lessonRepository, times(1)).findByChapterIdOrderByOrderNumberAsc(chapterId);

        ArgumentCaptor<Lesson> lessonCaptor = ArgumentCaptor.forClass(Lesson.class);
        verify(lessonRepository, times(2)).save(lessonCaptor.capture());

        List<Lesson> savedLessons = lessonCaptor.getAllValues();
        Lesson savedlesson2 = savedLessons.stream().filter(l -> l.getId() == 2L).findFirst().orElse(null);
        Lesson savedLesson3 = savedLessons.stream().filter(l -> l.getId() == 3L).findFirst().orElse(null);

        assertEquals(2, savedLessons.size());
        assertEquals(1, savedlesson2.getOrderNumber());
        assertEquals(2, savedLesson3.getOrderNumber());
    }

    @Test
    void deleteAndReorder_whenValidAndDeleteAtEnd_shouldDeleteLesson(){
        //arrange
        Lesson lesson1 = new Lesson(); lesson1.setId(2L); lesson1.setOrderNumber(1); lesson1.setChapter(chapter);
        Lesson lesson2 = new Lesson(); lesson2.setId(3L); lesson2.setOrderNumber(2); lesson2.setChapter(chapter);
        Long lessonIdToDelete = 3L;
        Lesson lessonToDelete = new Lesson(); lessonToDelete.setId(lessonIdToDelete); lessonToDelete.setOrderNumber(3);lessonToDelete.setChapter(chapter);
        List<Lesson> remainingLessons = new ArrayList<>(List.of(lesson1, lesson2));


        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(lessonRepository.findById(lessonIdToDelete)).thenReturn(Optional.of(lessonToDelete));
        when(lessonRepository.findByChapterIdOrderByOrderNumberAsc(chapterId)).thenReturn(remainingLessons);
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //act & assert
        this.lessonService.deleteAndReorder(chapterId, lessonIdToDelete);

        verify(chapterRepository, times(1)).findById(chapterId);
        verify(lessonRepository, times(1)).findById(lessonIdToDelete);
        verify(lessonRepository, times(1)).findByChapterIdOrderByOrderNumberAsc(chapterId);

        ArgumentCaptor<Lesson> lessonCaptor = ArgumentCaptor.forClass(Lesson.class);
        verify(lessonRepository, times(2)).save(lessonCaptor.capture());

        List<Lesson> savedLessons = lessonCaptor.getAllValues();
        Lesson savedlesson1 = savedLessons.stream().filter(l -> l.getId() == 2L).findFirst().orElse(null);
        Lesson savedlesson2 = savedLessons.stream().filter(l -> l.getId() == 3L).findFirst().orElse(null);

        assertEquals(2, savedLessons.size());
        assertEquals(1, savedlesson1.getOrderNumber());
        assertEquals(2, savedlesson2.getOrderNumber());
    }
}
