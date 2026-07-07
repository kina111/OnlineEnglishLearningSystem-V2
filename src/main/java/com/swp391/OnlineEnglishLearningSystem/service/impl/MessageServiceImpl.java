package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Message;
import com.swp391.OnlineEnglishLearningSystem.repository.MessageRepository;
import com.swp391.OnlineEnglishLearningSystem.service.MessageService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Override
    public Message findById(Long messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public List<Message> findAllByChatId(long chatId) {
        return List.of();
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}
