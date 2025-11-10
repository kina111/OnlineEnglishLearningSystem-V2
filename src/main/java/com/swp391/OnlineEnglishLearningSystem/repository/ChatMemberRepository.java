package com.swp391.OnlineEnglishLearningSystem.repository;

import com.swp391.OnlineEnglishLearningSystem.model.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    List<ChatMember> findByChatId(Long chatId);

    Optional<ChatMember> findByChatIdAndUserId(Long chatId, Long userId);

    boolean existsByChatIdAndUserId(Long chatId, Long userId);
}
