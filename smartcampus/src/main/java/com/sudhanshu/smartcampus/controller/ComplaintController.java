package com.sudhanshu.smartcampus.controller;

import com.sudhanshu.smartcampus.entity.Complaint;
import com.sudhanshu.smartcampus.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class ComplaintController {

    @Autowired
    private ComplaintService service;

    // Open complaint form
    @GetMapping("/complaint")
    public String complaintForm(Model model) {
        model.addAttribute("complaint", new Complaint());
        return "complaint-form";
    }

    // Save complaint
    @PostMapping("/submit")
public String submitComplaint(@ModelAttribute Complaint complaint, Model model) {

    complaint.setStatus("Pending");

    service.saveComplaint(complaint);

    String complaintId = "CS-2026-" + (1000 + complaint.getId());

    model.addAttribute("complaintId", complaintId);
    model.addAttribute("status", complaint.getStatus());
    model.addAttribute("priority", complaint.getPriority());

    return "complaint-success";
}

    // View all complaints
    @GetMapping("/list")
    public String complaintList(Model model) {
        model.addAttribute("complaints", service.getAllComplaints());
        return "complaint-list";
    }

    // Admin dashboard
    @GetMapping("/admin")
public String adminDashboard(Model model, HttpSession session) {

    Boolean loggedIn = (Boolean) session.getAttribute("adminLoggedIn");

    if (loggedIn == null || !loggedIn) {
        return "redirect:/admin-login";
    }

    model.addAttribute("adminName", session.getAttribute("adminName"));

    model.addAttribute("complaints", service.getAllComplaints());
    model.addAttribute("total", service.getTotalComplaints());
    model.addAttribute("pending", service.getPendingComplaints());
    model.addAttribute("inProgress", service.getInProgressComplaints());
    model.addAttribute("resolved", service.getResolvedComplaints());
    model.addAttribute("bcaCount", service.getDepartmentCount("BCA"));
    model.addAttribute("itCount", service.getDepartmentCount("IT"));
    model.addAttribute("cseCount", service.getDepartmentCount("CSE"));
    model.addAttribute("mbaCount", service.getDepartmentCount("MBA"));

    return "admin-dashboard";
}

    // Update complaint status
    @PostMapping("/update-status")
public String updateStatus(@RequestParam Long id,
                           @RequestParam String status,
                           HttpSession session) {

    service.updateStatus(id, status);

    session.setAttribute(
            "notification",
            "Your complaint CS-2026-" + (1000 + id)
                    + " has been updated to " + status + "."
    );

    return "redirect:/admin";
}
    // Open complaint tracker page
@GetMapping("/track")
public String trackComplaintPage() {
    return "track-complaint";
}

// Search complaint by ID
@PostMapping("/track")
public String trackComplaint(@RequestParam Long complaintId, Model model) {
    Complaint complaint = service.getComplaintById(complaintId);

    if (complaint != null) {
        model.addAttribute("complaint", complaint);
    } else {
        model.addAttribute("error", "Complaint ID not found");
    }

    return "track-complaint";
}
@GetMapping("/student-login")
public String studentLogin() {
    return "student-login";
}

@GetMapping("/student-dashboard")
public String studentDashboard(Model model, HttpSession session) {

    String studentName = "Sudhanshu Bhardwaj";

    model.addAttribute("studentName", studentName);
    model.addAttribute("department", "BCA");
    model.addAttribute("enrollment", "BCA24-1021");

    model.addAttribute("complaints",
            service.getComplaintsByStudent(studentName));

    String notification = (String) session.getAttribute("notification");

    if (notification != null) {
        model.addAttribute("notification", notification);
        session.removeAttribute("notification");
    }

    return "student-dashboard";
}
@GetMapping("/admin-login")
public String adminLogin() {
    return "admin-login";
}

@PostMapping("/admin-auth")
public String adminAuth(@RequestParam String adminId,
                        @RequestParam String password,
                        HttpSession session) {

    // Demo credentials
    if (adminId.equals("admin") && password.equals("admin123")) {

        session.setAttribute("adminLoggedIn", true);
        session.setAttribute("adminName", "Campus Administrator");

        return "redirect:/admin";
    }

    return "redirect:/admin-login?error";
}
@GetMapping("/admin-logout")
public String adminLogout(HttpSession session) {
    session.invalidate();
    return "redirect:/admin-login";
}
}