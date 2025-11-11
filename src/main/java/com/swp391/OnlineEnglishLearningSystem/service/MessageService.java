package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Message;

import java.util.List;

public interface MessageService {
    public void save(Message message);
    public Message findById(Long messageId);
    public void delete(Message message);
    public List<Message> findAllByChatId(long chatId);
    public List<Message> findAll();
}
