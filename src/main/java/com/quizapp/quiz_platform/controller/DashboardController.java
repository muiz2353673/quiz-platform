package com.quizapp.quiz_platform.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    
    @GetMapping("/")
    public String home() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}
