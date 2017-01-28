package com.ccdipersio;

import java.util.ArrayList;

class Grades {
    private int studentNumber;  // NUMBER TO REFERENCE STUDENT CORRESPONDING TO GRADE LIST
    private ArrayList<Assignment> grades = new ArrayList<>();  // ARRAY LIST FOR GRADES

    // CONSTRUCTOR
    Grades(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    // GETTERS
    ArrayList<Assignment> getGrades() {
        return grades;
    }


}
