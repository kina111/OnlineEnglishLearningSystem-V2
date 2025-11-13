package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Chat;
import com.swp391.OnlineEnglishLearningSystem.model.User;

import java.util.List;

public interface ChatService {
    public Chat createChat(Long userId1, Long userId2);
    public Chat findById(Long chatId);
    public void delete(Chat chat);
//    public List<Chat> findAllByUser(Long userId);
    public List<Chat> findAll();
    public Chat createGroupChat(Chat chat,List<User> members);
    public void deleteGroupChat(Chat chat);
    public Chat save(Chat chat);
    public Chat update(Chat chat);
    public Chat getOrCreateChat(Long userId1, Long userId2);
}
