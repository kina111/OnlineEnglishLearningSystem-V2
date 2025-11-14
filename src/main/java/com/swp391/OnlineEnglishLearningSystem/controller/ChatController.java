package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.Chat;
import com.swp391.OnlineEnglishLearningSystem.model.ChatMember;
import com.swp391.OnlineEnglishLearningSystem.model.Message;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.MessageDTO;
import com.swp391.OnlineEnglishLearningSystem.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;
    private final ChatMemberService chatMemberService;
    private final MessageService messageService;
    private final UserService userService;
    private final UploadService uploadService;


    public ChatController(ChatService chatService, ChatMemberService chatMemberService, MessageService messageService, UserService userService, UploadService uploadService) {
        this.chatService = chatService;
        this.chatMemberService = chatMemberService;
        this.messageService = messageService;
        this.userService = userService;
        this.uploadService = uploadService;
    }

    private User getUserById(HttpSession session){
        Long userId = (Long) session.getAttribute("currentUserId");
        return userService.getUserById(userId);
    }

    @GetMapping("/")
    public String getChatPage(HttpSession session, Model model){
        Long userId = (Long) session.getAttribute("currentUserId");
        if(userId == null){
            return "redirect:/";
        }
        User user = userService.getUserById(userId);
        List<ChatMember> chatMembers = chatMemberService.getChatMembersByUserId(userId);
        List<Chat> chats = new ArrayList<>();
        for(ChatMember chatMember : chatMembers){
            Chat chat = chatMember.getChat();
            if(chat.getType() == Chat.ChatType.PRIVATE){
                for(ChatMember member : chat.getMembers()){
                    if(!Objects.equals(member.getUser().getId(), userId)){
                        chat.setName(member.getUser().getFullName());
                        chat.setAvatar(member.getUser().getAvatar());
                    }
                }
            }
            chats.add(chat);
        }
        model.addAttribute("user", user);
        model.addAttribute("chats", chats);
        return "chat/index";
    }

    @GetMapping("/create-group")
    public String createChat(HttpSession session, Model model){
        Long userId = (Long) session.getAttribute("currentUserId");
        if(userId == null){
            return "redirect:/";
        }
        Chat chat = new Chat();
        chat.setType(Chat.ChatType.GROUP);
        model.addAttribute("chat", chat);
        User user = userService.getUserById(userId);
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("user", user);
        return "chat/create-group";
    }

    @PostMapping("/create-group")
    public String createChat(@ModelAttribute("chat") Chat chat,
                             @RequestParam("selectedUserIds") List<Long> selectedUserIds,
                             @RequestParam(value = "avatarFile",required = false)  MultipartFile avatarFile,
                             HttpSession session){

        Long userId = (Long) session.getAttribute("currentUserId");
        if(userId == null){
            return "redirect:/";
        }
        User user = userService.getUserById(userId);
        // Gán thành viên vào nhóm
        //Avatar
        if(avatarFile != null&&!avatarFile.isEmpty()){
            chat.setAvatar(uploadService.uploadImage(avatarFile, "chats/avatars/"));
        }
        List<User> members = userService.geUsersById(selectedUserIds);
        members.remove(user);
        chatService.createGroupChat(chat, members);
        ChatMember chatMember = new ChatMember();
        chatMember.setChat(chat);
        chatMember.setUser(user);

        chatMember.setRole(ChatMember.Role.ADMIN);
        chatMemberService.save(chatMember);
        return "redirect:/chats/";
    }


    @GetMapping("/private/{user2Id}")
    public String createPrivateChat(@PathVariable Long user2Id,HttpSession session){
        Long userId = (Long) session.getAttribute("currentUserId");

        if(userId != null&&user2Id != null&&userId != user2Id){
            Chat chat = chatService.getOrCreateChat(userId, user2Id);
            return "redirect:/chats/group/" + chat.getId();
        }
        return "redirect:/chats/";
    }

    @GetMapping("/group/{groupId}")
    public String groupChat(@PathVariable("groupId") Long groupId, Model model,HttpSession session){
        Chat chat = chatService.findById(groupId);
        User user = getUserById(session);
        if(chat == null||user == null||!chatMemberService.isChatMember(groupId, user.getId())){
            return "redirect:/chats/";
        }
        if(chat.getType() == Chat.ChatType.PRIVATE){
            for(ChatMember member : chat.getMembers()){
                if(member.getUser().getId() != user.getId()){
                    chat.setName(member.getUser().getFullName());
                    chat.setAvatar(member.getUser().getAvatar());
                }
            }
        }
        model.addAttribute("chat", chat);
        model.addAttribute("user", user);
        return "chat/groupChat";
    }

    @PostMapping("/group/{groupId}/send")
    public String sendGroupMessage(@PathVariable("groupId") Long groupId,
                                   @RequestParam String content,
                                   HttpSession session){
        Chat chat = chatService.findById(groupId);
        User user = getUserById(session);
        if(chat == null||user == null||!chatMemberService.isChatMember(groupId, user.getId())){
            return "redirect:/chats/";
        }
        Message message = new Message();
        message.setChat(chat);
        message.setSender(user);
        message.setContent(content);
        messageService.save(message);
//        sendMessage(message);
        return "redirect:/chats/group/"+groupId;
    }

    @GetMapping("/group/{groupId}/detail")
    public String groupChatDetail(@PathVariable("groupId") Long groupId, Model model,HttpSession session){
        Chat chat = chatService.findById(groupId);
        User user = getUserById(session);
        if(chat == null||user == null||!chatMemberService.isChatMember(groupId, user.getId())){
            return "redirect:/chats/";
        }
        model.addAttribute("chat", chat);
        model.addAttribute("user", user);
        return "chat/groupDetail";
    }

    @GetMapping("/group/{id}/edit")
    public String editGroup(@PathVariable Long id, Model model,HttpSession session) {
        Long userId = (Long) session.getAttribute("currentUserId");
        List<ChatMember> chatMembers = chatMemberService.getChatMembersByUserId(userId);
        ChatMember chatMember = new ChatMember();
        for(ChatMember cm : chatMembers){
            if(cm.getChat().getId() == id){
                chatMember = cm;
                break;
            }
        }
        if(chatMember.getRole() != ChatMember.Role.ADMIN){
            return "redirect:/chats/";
        }
        Chat chat = chatService.findById(id);
        model.addAttribute("updatedChat", chat);
        return "chat/update-group";
    }

    @PostMapping("/group/{id}/edit")
    public String updateGroup(@PathVariable Long id,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String description,
                              @RequestParam(value = "avatarFile",required = false)  MultipartFile avatarFile,
                              HttpSession session) {
        Long userId = (Long) session.getAttribute("currentUserId");
        List<ChatMember> chatMembers = chatMemberService.getChatMembersByUserId(userId);
        ChatMember chatMember = new ChatMember();
        for(ChatMember cm : chatMembers){
            if(cm.getChat().getId() == id){
                chatMember = cm;
                break;
            }
        }
        if(chatMember.getRole() != ChatMember.Role.ADMIN){
            return "redirect:/chats/";
        }
        Chat chat = chatService.findById(id);
        if(name != null&&!name.isBlank()){
            chat.setName(name);
        }
        if(description != null&&!description.isBlank()){
            chat.setDescription(description);
        }
        if(avatarFile != null&&!avatarFile.isEmpty()){
            chat.setAvatar(uploadService.uploadImage(avatarFile, "chats/avatars/"));
        }
        chatService.update(chat);
//        chatService.updateGroup(updatedChat, avatarFile);
        return "redirect:/chats/group/" + id;
    }

    @GetMapping("/group/{id}/out")
    public String leaveGroup(@PathVariable Long id, HttpSession session){
        User user = getUserById(session);
        Chat chat = chatService.findById(id);
        if(user == null||chat == null||chat.getType() != Chat.ChatType.GROUP){
            return "redirect:/";
        }
        chatMemberService.removeChatMember(id, user.getId());
        return "redirect:/chats/";
    }

    //SSE realtime

    private final Map<Long, Map<Long, SseEmitter>> groupEmitters = new ConcurrentHashMap<>();

    @GetMapping(value = "/subscribe/{groupId}/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long groupId, @PathVariable Long userId) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        groupEmitters.computeIfAbsent(groupId, g -> new ConcurrentHashMap<>()).put(userId, emitter);

        emitter.onCompletion(() -> removeEmitter(groupId, userId));
        emitter.onTimeout(() -> removeEmitter(groupId, userId));
        emitter.onError((e) -> removeEmitter(groupId, userId));

        try {
            emitter.send(SseEmitter.event().name("init").data("Connected to group " + groupId + " as " + userId));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    private void removeEmitter(Long groupId,Long userId) {
        Map<Long, SseEmitter> group = groupEmitters.get(groupId);
        if (group != null) {
            group.remove(userId);
            if (group.isEmpty()) {
                groupEmitters.remove(groupId);
            }
        }
    }

    @PostMapping("/sse/send")
    public String sendMessage( @RequestParam Long fromUser,
                               @RequestParam Long toGroup,
                               @RequestParam(required = false) String content,
                               @RequestParam(required = false) MultipartFile image) {

        Chat groupChat = chatService.findById(toGroup);
        if (groupChat == null) return "No active group: " + toGroup;
        String imageUrl = null;
        if(image != null&&!image.isEmpty()){
            imageUrl = uploadService.uploadImage(image, "chats/"+toGroup+"/images/");
        }

        MessageDTO msgDto = new MessageDTO();
        Message msg = new Message();
        User user = userService.getUserById(fromUser);
        msgDto.setFromUser(fromUser);
        msgDto.setUserName(user.getFullName());
        msgDto.setUserAvatarUrl(user.getAvatar());
        msgDto.setContent(content);
        msgDto.setTimestamp(LocalDateTime.now());
        msgDto.setFileUrl(imageUrl);

        msg.setSender(user);
        msg.setContent(content);
        msg.setChat(groupChat);
        msg.setFileURl(imageUrl);
        messageService.save(msg);

        Map<Long, SseEmitter> group = groupEmitters.get(msg.getChat().getId());
        if (group == null) return "No active group: " + msg.getChat().getId();

        for (Map.Entry<Long, SseEmitter> entry : group.entrySet()) {
            try {
                entry.getValue().send(SseEmitter.event().name("message").data(msgDto));
            } catch (IOException e) {
                entry.getValue().completeWithError(e);
            }
        }

        return "Message sent to group " + msg.getChat().getId();
    }

    @GetMapping("/{groupId}/invite")
    public String inviteMember(@PathVariable Long groupId, HttpSession session, Model model){
        Long userId = (Long) session.getAttribute("currentUserId");
        Chat chat = chatService.findById(groupId);
//        ChatMember chatMember = ;
        if(chat == null||!chatMemberService.isChatMember(groupId, userId)){
            return "redirect:/chats/";
        }
        List<User> users = new ArrayList<>();
        for(User user : userService.getAllUsers()){
            if(!chatMemberService.isChatMember(groupId, user.getId())){
                users.add(user);
            }
        }
        model.addAttribute("users",users);
        model.addAttribute("chatMembers", chat.getMembers());
        model.addAttribute("chatId", chat.getId());
        model.addAttribute("chat", chat);
        return "chat/invite-member";
    }

    @GetMapping("/group/{groupId}/{userId}/remove")
    public String removeMember(@PathVariable Long groupId,
                               @PathVariable Long userId){
        Chat chat = chatService.findById(groupId);
        if(chat == null||!chatMemberService.isChatMember(groupId, userId)){
            return "redirect:/chats/";
        }
        chatMemberService.removeChatMember(groupId, userId);
        return "redirect:/chats/group/"+groupId;
    }

    @PostMapping("/{groupId}/invite")
    public String inviteMember(@PathVariable Long groupId,
                               @RequestParam List<Long> selectedUserIds,
                               HttpSession session){
        Long currentUserId = (Long) session.getAttribute("currentUserId");
        Chat chat = chatService.findById(groupId);
        if(chat != null&&chatMemberService.isChatMember(groupId, currentUserId)){
            for(Long userId : selectedUserIds){
                if(!chatMemberService.isChatMember(groupId, userId)){
                    ChatMember chatMember = new ChatMember();
                    chatMember.setChat(chat);
                    chatMember.setUser(userService.getUserById(userId));
                    chatMember.setRole(ChatMember.Role.MEMBER);
                    chatMemberService.save(chatMember);
                }
            }
        }
        return "redirect:/chats/group/"+groupId;
    }

}
