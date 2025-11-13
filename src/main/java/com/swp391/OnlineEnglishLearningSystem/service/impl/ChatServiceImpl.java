package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Chat;
import com.swp391.OnlineEnglishLearningSystem.model.ChatMember;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.repository.ChatMemberRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.ChatRepository;
import com.swp391.OnlineEnglishLearningSystem.service.ChatMemberService;
import com.swp391.OnlineEnglishLearningSystem.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository, ChatMemberRepository chatMemberRepository) {
        this.chatRepository = chatRepository;
        this.chatMemberRepository = chatMemberRepository;
    }

    @Override
    public Chat createChat(Long userId1, Long userId2) {
        Chat chat = new Chat();
        chat.setType(Chat.ChatType.PRIVATE);
        return chatRepository.save(chat);
    }

    @Override
    public Chat findById(Long chatId) {
        return chatRepository.findById(chatId).orElse(null);
    }

    @Override
    public void delete(Chat chat) {
        chatRepository.delete(chat);
    }

    @Override
    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    @Override
    public Chat createGroupChat(Chat chat, List<User> members) {
        chat.setType(Chat.ChatType.GROUP);
        Chat savedChat = chatRepository.save(chat);

        for (User user : members) {
            ChatMember member = new ChatMember();
            member.setChat(savedChat);
            member.setUser(user);
            chatMemberRepository.save(member);
        }

        return savedChat;
    }

    @Override
    public void deleteGroupChat(Chat chat) {
        chatRepository.delete(chat);
    }

    @Override
    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public Chat update(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public Chat getOrCreateChat(Long userId1, Long userId2) {
        return chatRepository.findPrivateChatBetween(userId1, userId2)
                .orElseGet(() -> {
                    Chat newChat = new Chat();
                    newChat.setType(Chat.ChatType.PRIVATE);
                    Chat saved = chatRepository.save(newChat);

                    chatMemberRepository.save(new ChatMember(saved, new User(userId1), ChatMember.Role.MEMBER));
                    chatMemberRepository.save(new ChatMember(saved, new User(userId2), ChatMember.Role.MEMBER));

                    return saved;
                });
    }


}
