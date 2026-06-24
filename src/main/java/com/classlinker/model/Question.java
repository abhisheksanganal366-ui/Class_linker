package com.classlinker.model;

public class Question {
    private int id;
    private int studentId;
    private String studentName;
    private String rollNumber;
    private String question;
    private String answer;
    private String createdAt;
    private String answeredAt;

    public Question() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getAnsweredAt() { return answeredAt; }
    public void setAnsweredAt(String answeredAt) { this.answeredAt = answeredAt; }
}
