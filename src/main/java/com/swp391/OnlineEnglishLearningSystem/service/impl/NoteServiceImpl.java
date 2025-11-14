package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Note;
import com.swp391.OnlineEnglishLearningSystem.model.UserLesson;
import com.swp391.OnlineEnglishLearningSystem.model.dto.NoteDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.NoteRequest;
import com.swp391.OnlineEnglishLearningSystem.repository.NoteRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.UserLessonRepository;
import com.swp391.OnlineEnglishLearningSystem.service.NoteService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final UserLessonRepository userLessonRepository;

    public NoteServiceImpl(NoteRepository noteRepository, UserLessonRepository userLessonRepository) {
        this.noteRepository = noteRepository;
        this.userLessonRepository = userLessonRepository;
    }


    @Override
    public Note createNew(Long userId, long lessonId, NoteRequest noteRequest) {
        UserLesson current = this.userLessonRepository.findByUserIdAndLessonId(userId, lessonId).orElseThrow(() -> new IllegalArgumentException("UserLesson not found"));
        return this.noteRepository.save(new Note(current, noteRequest));
    }

    @Override
    public List<NoteDTO> createDtosByChapterAndEnrollmentId(long chapterId, long enrollmentId, Sort sort) {
        return this.noteRepository.findNotesByChapterAndEnrollmentId(chapterId, enrollmentId, sort);
    }

    @Override
    public void deleteById(long noteId) {
        this.noteRepository.deleteById(noteId);
    }

    @Override
    public Note updateById(long noteId, String content) {
        Note n = this.noteRepository.findById(noteId).orElseThrow(() -> new IllegalArgumentException("Note not found"));
        n.setContent(content);
        return this.noteRepository.save(n);
    }
}
