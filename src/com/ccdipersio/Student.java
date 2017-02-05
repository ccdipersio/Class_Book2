package com.ccdipersio;

/**
 * This is the Student class.
 * This doesn't have any functions that actually do anything besides constructing and getting.
 */

class Student {
    private String name;
    private int studentNumber;
    private int gradeLevel;

    /**
     * Constructor.
     * @param name  String  Name of Student.
     * @param studentNumber int Number of Student
     * @param gradeLevel    int gradeLevel of Student
     */
    Student(String name, int studentNumber, int gradeLevel) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.gradeLevel = gradeLevel;
    }

    /**
     * Getters.
     */
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