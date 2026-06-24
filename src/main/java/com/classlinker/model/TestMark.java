package com.classlinker.model;

public class TestMark {
    private int id;
    private int testId;
    private int studentId;
    private String studentName;
    private String rollNumber;
    private int marks;
    private int totalMarks;
    private String testName;

    public TestMark() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTestId() { return testId; }
    public void setTestId(int testId) { this.testId = testId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }

    public int getTotalMarks() { return totalMarks; }
    public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }

    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
}
