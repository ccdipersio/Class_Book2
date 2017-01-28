package com.ccdipersio;

/**
 * Assignment Type Integer Definitions
 * 0 - Final Exam
 * 1 - Midterm Exam
 * 2 - Test
 * 3 - Quiz
 * 4 - Essay
 * 5 - Presentation
 * 6 - Other Project
 * 7 - Homework
 * 8 - Classwork
 * 9 - Participation
 */
class Assignment {
    private String name;  // ASSIGNMENT'S NAME
    private int type;  // TYPE INTEGER (definitions above)
    private String typeToStringConverted;  // TYPE'S ASSOCIATED STRING VALUE (definitions above)
    private String dateOfAssignment;  // DATE OF ASSIGNMENT
    private int achievedScore;  // SCORE ACHIEVED BY THE STUDENT
    private int maxScore;  // MAXIMUM POSSIBLE SCORE ON ASSIGNMENT


    // CONSTRUCTOR
    Assignment(String name, int type, String dateOfAssignment, int achievedScore, int maxScore) {
        this.name = name;
        this.type = type;
        this.typeToStringConverted = typeToStringConvert();
        this.dateOfAssignment = dateOfAssignment;
        this.achievedScore = achievedScore;
        this.maxScore = maxScore;
    }


    // GETTERS
    String getName() {
        return name;
    }
    int getType() {
        return type;
    }
    String getTypeToStringConverted() {
        return typeToStringConverted;
    }
    String getDateOfAssignment() {
        return dateOfAssignment;
    }
    int getAchievedScore() {
        return achievedScore;
    }
    int getMaxScore() {
        return maxScore;
    }

    // OTHER FUNCTIONS
    // CONVERT TYPE INTEGER TO ASSOCIATED STRING
    private String typeToStringConvert() {
        switch(this.type) {
            case 0:
                return "Final Exam";
            case 1:
                return "Midterm Exam";
            case 2:
                return "Test";
            case 3:
                return "Quiz";
            case 4:
                return "Essay";
            case 5:
                return "Presentation";
            case 6:
                return "Other Project";
            case 7:
                return "Homework";
            case 8:
                return "Classwork";
            case 9:
                return "Participation";
        }
        return "ERROR";
    }
}
