package com.ccdipersio;

class Student {
    private String name;  // STUDENT'S NAME
    private int studentNumber;  // STUDENT'S CLASS NUMBER
    private int gradeLevel;  // STUDENT'S GRADE LEVEL

    // CONSTRUCTOR
    Student(String name, int studentNumber, int gradeLevel) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.gradeLevel = gradeLevel;
    }

    // GETTERS
    String getName() {
        return name;
    }
    int getStudentNumber() {
        return studentNumber;
    }
    int getGradeLevel() {
        return gradeLevel;
    }
}