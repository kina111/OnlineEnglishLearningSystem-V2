package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.ChatMember;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.repository.ChatMemberRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.ChatRepository;
import com.swp391.OnlineEnglishLearningSystem.service.ChatMemberService;
import com.swp391.OnlineEnglishLearningSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMemberServiceImpl implements ChatMemberService {
    private final ChatMemberRepository chatMemberRepository;
    private final ChatRepository chatRepository;
    private final UserService userService;

    @Autowired
    public ChatMemberServiceImpl(ChatMemberRepository chatMemberRepository, ChatRepository chatRepository, UserService userService) {
        this.chatMemberRepository = chatMemberRepository;
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    @Override
    public void addChatMember(Long chatId, Long userId) {
        ChatMember chatMember = new ChatMember();
        chatMember.setChat(chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException("Chat not found")));
        chatMember.setUser(userService.getById(userId));
        chatMemberRepository.save(chatMember);
    }

    @Override
    public void removeChatMember(Long chatId, Long userId) {
        List<ChatMember> chatMembers = chatMemberRepository.findByChatId(chatId);
        for(ChatMember chatMember : chatMembers){
            if(chatMember.getUser().getId() == userId){
                chatMemberRepository.delete(chatMember);
                break;
            }
        }
//        ChatMember chatMember = chatMemberRepository.findByChatIdAndUserId(chatId, userId).orElseThrow(() -> new IllegalArgumentException("ChatMember not found"));
    }

    @Override
    public boolean isChatMember(Long chatId, Long userId) {
        List<ChatMember> chatMembers = chatMemberRepository.findByChatId(chatId);
        for(ChatMember chatMember : chatMembers){
            if(chatMember.getUser().getId() == userId){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ChatMember> getChatMembers() {
        return chatMemberRepository.findAll();
    }

    @Override
    public List<ChatMember> getChatMembersByUserId(Long userId) {
        List<ChatMember> chatMembers = new ArrayList<>();
        for(ChatMember chatMember : getChatMembers()){
            if(chatMember.getUser().getId().equals(userId)){
                chatMembers.add(chatMember);
            }
        }
        return chatMembers;
    }

    @Override
    public ChatMember save(ChatMember chatMember) {
        return chatMemberRepository.save(chatMember);
    }


}
