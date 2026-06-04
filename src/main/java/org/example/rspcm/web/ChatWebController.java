package org.example.rspcm.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.rspcm.model.entity.Role;
import org.example.rspcm.model.entity.User;
import org.example.rspcm.model.enums.RoleName;
import org.example.rspcm.service.ChatMessageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatWebController {

    private final ChatMessageService chatMessageService;

    @GetMapping
    public String chatList(HttpServletRequest request, Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("chats", chatMessageService.getMyChats(user.getEmail()));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("jwtToken", extractToken(request));
        model.addAttribute("activePage", "chat");
        model.addAttribute("sidebarRole", resolveSidebarRole(user));
        return "chat/list";
    }

    @GetMapping("/{chatId}")
    public String chatDetail(@PathVariable Long chatId, HttpServletRequest request,
                              Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("chats", chatMessageService.getMyChats(user.getEmail()));
            model.addAttribute("messages", chatMessageService.getChatMessages(chatId, user.getEmail()));
            model.addAttribute("members", chatMessageService.getChatMembers(chatId, user.getEmail()));
            model.addAttribute("chatId", chatId);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("jwtToken", extractToken(request));
        model.addAttribute("activePage", "chat");
        model.addAttribute("sidebarRole", resolveSidebarRole(user));
        return "chat/detail";
    }

    private String extractToken(HttpServletRequest request) {
        if (request.getCookies() == null) return "";
        for (Cookie c : request.getCookies()) {
            if ("auth_token".equals(c.getName())) return c.getValue();
        }
        return "";
    }

    private String resolveSidebarRole(User user) {
        for (Role role : user.getRoles()) {
            if (role.getRoleName() == RoleName.ROLE_ADMIN) return "admin";
            if (role.getRoleName() == RoleName.ROLE_TEACHER) return "teacher";
        }
        return "student";
    }
}
