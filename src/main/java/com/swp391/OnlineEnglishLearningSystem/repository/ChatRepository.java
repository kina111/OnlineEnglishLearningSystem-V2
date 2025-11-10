package com.swp391.OnlineEnglishLearningSystem.repository;


import com.swp391.OnlineEnglishLearningSystem.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

//    @Query("""
//        SELECT c FROM Chat c
//        JOIN c.members m1
//        JOIN c.members m2
//        WHERE c.type = 'PRIVATE'
//          AND m1.users.id = :userId1
//          AND m2.users.id = :userId2
//    """)
//    Optional<Chat> findPrivateChatBetween(Long userId1, Long userId2);
//
//    @Query("SELECT c FROM Chat c JOIN c.members m WHERE m.users.id = :userId")
//    List<Chat> findAllByUserId(Long userId);
}
