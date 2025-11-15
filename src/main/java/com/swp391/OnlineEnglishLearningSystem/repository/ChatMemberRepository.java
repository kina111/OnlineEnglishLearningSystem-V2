package com.swp391.OnlineEnglishLearningSystem.repository;

import com.swp391.OnlineEnglishLearningSystem.model.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    List<ChatMember> findByChatId(Long chatId);

    Optional<ChatMember> findByChatIdAndUserId(Long chatId, Long userId);

    boolean existsByChatIdAndUserId(Long chatId, Long userId);

    @Query("SELECT cm FROM ChatMember cm WHERE cm.chat.id = :chatId AND cm.role = 'ADMIN'")
    List<ChatMember> findAdmins(Long chatId);

    @Query("SELECT cm FROM ChatMember cm WHERE cm.chat.id = :chatId ORDER BY cm.joinedAt ASC")
    List<ChatMember> findMembersOrderByJoinedAt(Long chatId);
}
