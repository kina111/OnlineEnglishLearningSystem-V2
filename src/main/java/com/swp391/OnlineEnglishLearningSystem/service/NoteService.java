package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Note;
import com.swp391.OnlineEnglishLearningSystem.model.dto.NoteDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.NoteRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface NoteService {
    Note createNew(Long userId, long lessonId, NoteRequest noteRequest);

    List<NoteDTO> createDtosByChapterAndEnrollmentId(long chapterId, long enrollmentId, Sort sort);

    void deleteById(long noteId);

    Note updateById(long noteId, String content);
}
