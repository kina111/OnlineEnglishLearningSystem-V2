package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.ChatMember;

import java.util.List;

public interface ChatMemberService {
    public void addChatMember(Long chatId, Long userId);
    public void removeChatMember(Long chatId, Long userId);
    public boolean isChatMember(Long chatId, Long userId);
    public List<ChatMember> getChatMembers();
    public List<ChatMember> getChatMembersByUserId(Long userId);
    public ChatMember save(ChatMember chatMember);
}
