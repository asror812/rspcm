package org.example.rspcm.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.rspcm.dto.auth.AuthResponse;
import org.example.rspcm.dto.auth.LoginRequest;
import org.example.rspcm.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginWebController {

    private final AuthService authService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(
            @RequestParam String identifier,
            @RequestParam String password,
            HttpServletResponse response,
            Model model) {
        try {
            LoginRequest req = new LoginRequest(identifier, password);
            AuthResponse auth = authService.login(req);

            Cookie cookie = new Cookie("auth_token", auth.token());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(7200); // 2 hours
            response.addCookie(cookie);

            // Redirect based on role
            if (auth.roles().contains("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard";
            } else if (auth.roles().contains("ROLE_TEACHER")) {
                return "redirect:/teacher/dashboard";
            } else if (auth.roles().contains("ROLE_STUDENT")) {
                return "redirect:/student/dashboard";
            }
            return "redirect:/login?error=unknown_role";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid credentials. Please try again.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("auth_token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login";
    }
}
