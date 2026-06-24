package com.classlinker.controller;

import com.classlinker.model.User;
import com.classlinker.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            User u = (User) session.getAttribute("user");
            return u.getRole().equals("lecturer") ? "redirect:/lecturer/dashboard" : "redirect:/student/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session,
                          RedirectAttributes ra) {
        Optional<User> opt = userRepository.findByEmailAndPassword(email, password);
        if (opt.isPresent()) {
            session.setAttribute("user", opt.get());
            return opt.get().getRole().equals("lecturer") ? "redirect:/lecturer/dashboard" : "redirect:/student/dashboard";
        }
        ra.addFlashAttribute("error", "Invalid email or password");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String phone,
                             @RequestParam String rollNumber,
                             RedirectAttributes ra) {
        if (userRepository.existsByEmail(email)) {
            ra.addFlashAttribute("error", "Email already exists");
            return "redirect:/register";
        }
        userRepository.registerStudent(name, email, password, phone, rollNumber);
        ra.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
