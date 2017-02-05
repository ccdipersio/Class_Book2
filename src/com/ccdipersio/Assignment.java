package com.ccdipersio;

/**
 * This is the Assignment class.
 * Knowing what the Assignment Type indexes are is vital to correct usages. Those indexes are:
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
    private String name;
    private int type;
    private String typeToStringConverted;
    private String dateOfAssignment;
    private int achievedScore;
    private int maxScore;


    /**
     * Constructor.
     * @param name  String  Name of the Assignment.
     * @param type  int Integer referring to the type of Assignment.
     * @param dateOfAssignment  String  Date of the Assignment.
     * @param achievedScore int Student's Score on the Assignment.
     * @param maxScore  int Maximum possible Score on the Assignment.
     */
    Assignment(String name, int type, String dateOfAssignment, int achievedScore, int maxScore) {
        this.name = name;
        this.type = type;
        this.typeToStringConverted = typeToStringConvert();
        this.dateOfAssignment = dateOfAssignment;
        this.achievedScore = achievedScore;
        this.maxScore = maxScore;
    }

    /**
     * Getters.
     */
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

    /**
     * Defines the Name of the Type of Assignment based on the Type Integer.
     * @return  String  Name of the Type of the Assignment; else if a bad Integer is passed, "ERROR" is returned.
     */
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
