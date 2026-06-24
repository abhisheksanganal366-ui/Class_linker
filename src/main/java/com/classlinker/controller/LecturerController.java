package com.classlinker.controller;

import com.classlinker.model.*;
import com.classlinker.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lecturer")
public class LecturerController {

    @Autowired private AnnouncementRepository announcementRepo;
    @Autowired private ResourceRepository resourceRepo;
    @Autowired private AssignmentRepository assignmentRepo;
    @Autowired private TestRepository testRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private QuestionRepository questionRepo;

    private User getUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    private boolean isLecturer(HttpSession session) {
        User u = getUser(session);
        return u != null && "lecturer".equals(u.getRole());
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isLecturer(session)) return "redirect:/login";
        model.addAttribute("user", getUser(session));
        return "lecturer/dashboard";
    }

    // ── Announcements ──
    @GetMapping("/announcements")
    public String announcements(HttpSession session, Model model) {
        if (!isLecturer(session)) return "redirect:/login";
        model.addAttribute("announcements", announcementRepo.findAll());
        model.addAttribute("user", getUser(session));
        return "lecturer/announcements";
    }

    @PostMapping("/announcements/add")
    public String addAnnouncement(@RequestParam String title, @RequestParam String content,
                                  HttpSession session, RedirectAttributes ra) {
        if (!isLecturer(session)) return "redirect:/login";
        announcementRepo.add(title, content, getUser(session).getId());
        ra.addFlashAttribute("success", "Announcement added!");
        return "redirect:/lecturer/announcements";
    }

    @PostMapping("/announcements/edit")
    public String editAnnouncement(@RequestParam int id, @RequestParam String title,
                                   @RequestParam String content, HttpSession session) {
        if (!isLecturer(session)) return "redirect:/login";
        announcementRepo.update(id, title, content);
        return "redirect:/lecturer/announcements";
    }

    @GetMapping("/announcements/delete/{id}")
    public String deleteAnnouncement(@PathVariable int id, HttpSession session) {
        if (!isLecturer(session)) return "redirect:/login";
        announcementRepo.delete(id);
        return "redirect:/lecturer/announcements";
    }

    // ── Resources ──
    @GetMapping("/resources")
    public String resources(HttpSession session, Model model) {
        if (!isLecturer(session)) return "redirect:/login";
        model.addAttribute("resources", resourceRepo.findAll());
        model.addAttribute("user", getUser(session));
        return "lecturer/resources";
    }

    @PostMapping("/resources/add")
    public String addResource(@RequestParam String name, @RequestParam String description,
                              @RequestParam String fileType, @RequestParam String fileLink,
                              HttpSession session) {
        if (!isLecturer(session)) return "redirect:/login";
        resourceRepo.add(name, description, fileType, fileLink, getUser(session).getId());
        return "redirect:/lecturer/resources";
    }

    @GetMapping("/resources/delete/{id}")
    public String deleteResource(@PathVariable int id, HttpSession session) {
        if (!isLecturer(session)) return "redirect:/login";
        resourceRepo.delete(id);
        return "redirect:/lecturer/resources";
    }

    // ── Assignments ──
    @GetMapping("/assignments")
    public String assignments(HttpSession session, Model model) {
        if (!isLecturer(session)) return "redirect:/login";
        model.addAttribute("assignments", assignmentRepo.findAll());
        model.addAttribute("user", getUser(session));
        return "lecturer/assignments";
    }

    @PostMapping("/assignments/add")
    public String addAssignment(@RequestParam String title, @RequestParam String description,
                                @RequestParam String dueDate, @RequestParam int totalMarks,
                                HttpSession session) {
        if (!isLecturer(session)) return "redirect:/login";
        assignmentRepo.add(title, description, dueDate, totalMarks, getUser(session).getId());
        return "redirect:/lecturer/assignments";
    }

    @GetMapping("/assignments/{id}/submissions")
    public String viewSubmissions(@PathVariable int id, HttpSession session, Model model) {
        if (!isLecturer(session)) return "redirect:/login";
        Assignment a = assignmentRepo.findById(id);
        model.addAttribute("assignment", a);
        model.addAttribute("submissions", assignmentRepo.getSubmissions(id));
        model.addAttribute("user", getUser(session));
        return "lecturer/submissions";
    }

    @PostMapping("/submissions/update")
    public String updateSubmission(@RequestParam int id, @RequestParam String status,
                                   @RequestParam int marks, @RequestParam int assignmentId,
                                   HttpSession session) {
        if (!isLecturer(session)) return "redirect:/login";
        assignmentRepo.updateSubmission(id, status, marks);
        return "redirect:/lecturer/assignments/" + assignmentId + "/submissions";
    }

    // ── Tests ──
    @GetMapping("/tests")
    public String tests(HttpSession session, Model model) {
        if (!isLecturer(session)) return "redirect:/login";
        model.addAttribute("tests", testRepo.findAll());
        model.addAttribute("user", getUser(session));
        return "lecturer/tests";
    }

    @PostMapping("/tests/add")
    public String addTest(@RequestParam String testName, @RequestParam int totalMarks,
                          HttpSession session) {
        if (!isLecturer(session)) return "redirect:/login";
        testRepo.add(testName, totalMarks, getUser(session).getId());
        return "redirect:/lecturer/tests";
    }

    @GetMapping("/tests/{id}/marks")
    public String viewMarks(@PathVariable int id, HttpSession session, Model model) {
        if (!isLecturer(session)) return "redirect:/login";
        Test t = testRepo.findById(id);
        model.addAttribute("test", t);
        model.addAttribute("marks", testRepo.getMarks(id));
        model.addAttribute("user", getUser(session));
        return "lecturer/testmarks";
    }

    @PostMapping("/tests/marks/update")
    public String updateMark(@RequestParam int id, @RequestParam int marks,
                             @RequestParam int testId, HttpSession session) {
        if (!isLecturer(session)) return "redirect:/login";
        testRepo.updateMark(id, marks);
        return "redirect:/lecturer/tests/" + testId + "/marks";
    }

    // ── Students ──
    @GetMapping("/students")
    public String students(HttpSession session, Model model) {
        if (!isLecturer(session)) return "redirect:/login";
        model.addAttribute("students", userRepo.getAllStudents());
        model.addAttribute("user", getUser(session));
        return "lecturer/students";
    }

    @PostMapping("/students/add")
    public String addStudent(@RequestParam String name, @RequestParam String email,
                             @RequestParam String phone, @RequestParam String rollNumber,
                             HttpSession session, RedirectAttributes ra) {
        if (!isLecturer(session)) return "redirect:/login";
        if (userRepo.existsByEmail(email)) {
            ra.addFlashAttribute("error", "Email already exists");
        } else {
            userRepo.addStudentByLecturer(name, email, phone, rollNumber);
            ra.addFlashAttribute("success", "Student added! Default password: student123");
        }
        return "redirect:/lecturer/students";
    }

    @PostMapping("/students/edit")
    public String editStudent(@RequestParam int id, @RequestParam String name,
                              @RequestParam String email, @RequestParam String phone,
                              @RequestParam String rollNumber, HttpSession session) {
        if (!isLecturer(session)) return "redirect:/login";
        userRepo.updateStudent(id, name, email, phone, rollNumber);
        return "redirect:/lecturer/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable int id, HttpSession session) {
        if (!isLecturer(session)) return "redirect:/login";
        userRepo.deleteStudent(id);
        return "redirect:/lecturer/students";
    }

    // ── Q&A ──
    @GetMapping("/qa")
    public String qa(HttpSession session, Model model) {
        if (!isLecturer(session)) return "redirect:/login";
        model.addAttribute("questions", questionRepo.findAll());
        model.addAttribute("user", getUser(session));
        return "lecturer/qa";
    }

    @PostMapping("/qa/answer")
    public String answerQuestion(@RequestParam int id, @RequestParam String answer,
                                 HttpSession session) {
        if (!isLecturer(session)) return "redirect:/login";
        questionRepo.answer(id, answer);
        return "redirect:/lecturer/qa";
    }

    @GetMapping("/qa/delete/{id}")
    public String deleteQuestion(@PathVariable int id, HttpSession session) {
        if (!isLecturer(session)) return "redirect:/login";
        questionRepo.delete(id);
        return "redirect:/lecturer/qa";
    }
}
