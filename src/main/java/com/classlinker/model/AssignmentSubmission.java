package com.classlinker.model;

public class AssignmentSubmission {
    private int id;
    private int assignmentId;
    private int studentId;
    private String studentName;
    private String rollNumber;
    private String status;
    private int marks;

    public AssignmentSubmission() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAssignmentId() { return assignmentId; }
    public void setAssignmentId(int assignmentId) { this.assignmentId = assignmentId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }
}
