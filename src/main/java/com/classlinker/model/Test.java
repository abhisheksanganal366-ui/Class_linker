package com.classlinker.model;

public class Test {
    private int id;
    private String testName;
    private int totalMarks;
    private String createdAt;

    public Test() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }

    public int getTotalMarks() { return totalMarks; }
    public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
