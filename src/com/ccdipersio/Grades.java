package com.ccdipersio;

import java.util.ArrayList;

/**
 * This is the Grades class.
 * This doesn't have any functions that actually do anything besides constructing and getting.
 */

class Grades {
    private int studentNumber;
    private ArrayList<Assignment> grades = new ArrayList<>();

    /**
     * Constructor.
     * @param studentNumber int Number of the Student.
     */
    Grades(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    /**
     * Getter.
     */
    ArrayList<Assignment> getGrades() {
        return grades;
    }


}
