package com.classlinker.controller;

import com.classlinker.model.User;
import com.classlinker.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired private AnnouncementRepository announcementRepo;
    @Autowired private ResourceRepository resourceRepo;
    @Autowired private AssignmentRepository assignmentRepo;
    @Autowired private TestRepository testRepo;
    @Autowired private QuestionRepository questionRepo;

    private User getUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    private boolean isStudent(HttpSession session) {
        User u = getUser(session);
        return u != null && "student".equals(u.getRole());
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";
        model.addAttribute("user", getUser(session));
        return "student/dashboard";
    }

    @GetMapping("/announcements")
    public String announcements(HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";
        model.addAttribute("announcements", announcementRepo.findAll());
        model.addAttribute("user", getUser(session));
        return "student/announcements";
    }

    @GetMapping("/resources")
    public String resources(@RequestParam(required = false) String search,
                            HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";
        if (search != null && !search.isBlank()) {
            model.addAttribute("resources", resourceRepo.search(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("resources", resourceRepo.findAll());
        }
        model.addAttribute("user", getUser(session));
        return "student/resources";
    }

    @GetMapping("/assignments")
    public String assignments(HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";
        User u = getUser(session);
        model.addAttribute("assignments", assignmentRepo.getStudentAssignments(u.getId()));
        model.addAttribute("user", u);
        return "student/assignments";
    }

    @GetMapping("/marks")
    public String marks(HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";
        User u = getUser(session);
        model.addAttribute("marks", testRepo.getStudentMarks(u.getId()));
        model.addAttribute("user", u);
        return "student/marks";
    }

    @GetMapping("/questions")
    public String questions(HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";
        User u = getUser(session);
        model.addAttribute("questions", questionRepo.getByStudent(u.getId()));
        model.addAttribute("user", u);
        return "student/questions";
    }

    @PostMapping("/questions/ask")
    public String askQuestion(@RequestParam String question, HttpSession session) {
        if (!isStudent(session)) return "redirect:/login";
        questionRepo.add(getUser(session).getId(), question);
        return "redirect:/student/questions";
    }
}
