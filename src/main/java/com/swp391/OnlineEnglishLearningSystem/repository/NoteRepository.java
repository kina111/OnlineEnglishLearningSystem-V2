package com.swp391.OnlineEnglishLearningSystem.repository;

import com.swp391.OnlineEnglishLearningSystem.model.Note;
import com.swp391.OnlineEnglishLearningSystem.model.dto.NoteDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query("SELECT NEW com.swp391.OnlineEnglishLearningSystem.model.dto.NoteDTO( " +
            "    n.id, " +
            "    n.content, " +
            "    n.timeAtLesson, " +
            "    l.title " +
            ") " +
            "FROM Note n " +
            "JOIN n.userLesson ul " +
            "JOIN ul.lesson l " +
            "JOIN l.chapter c " +
            "WHERE l.chapter.id = :chapterId AND ul.enrollment.id = :enrollmentId")
    List<NoteDTO> findNotesByChapterAndEnrollmentId(
            @Param("chapterId") Long chapterId,
            @Param("enrollmentId") Long enrollmentId,
            Sort sort
    );
}
