package com.sudhanshu.smartcampus.service;

import com.sudhanshu.smartcampus.entity.Complaint;
import com.sudhanshu.smartcampus.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository repository;

    public Complaint saveComplaint(Complaint complaint) {
        return repository.save(complaint);
    }

    public List<Complaint> getAllComplaints() {
        return repository.findAll();
    }

    public Complaint updateStatus(Long id, String status) {
        Complaint complaint = repository.findById(id).orElseThrow();
        complaint.setStatus(status);
        return repository.save(complaint);
    }
    public long getTotalComplaints() {
    return repository.count();
}

public long getPendingComplaints() {
    return repository.findAll().stream()
            .filter(c -> "Pending".equals(c.getStatus()))
            .count();
}

public long getInProgressComplaints() {
    return repository.findAll().stream()
            .filter(c -> "In Progress".equals(c.getStatus()))
            .count();
}

public long getResolvedComplaints() {
    return repository.findAll().stream()
            .filter(c -> "Resolved".equals(c.getStatus()))
            .count();
}
public Complaint getComplaintById(Long id) {
    return repository.findById(id).orElse(null);
}
public List<Complaint> getComplaintsByStudent(String studentName) {
    return repository.findByStudentName(studentName);
}
public long getDepartmentCount(String department) {
    return repository.findAll()
            .stream()
            .filter(c -> department.equalsIgnoreCase(c.getDepartment()))
            .count();
}
}