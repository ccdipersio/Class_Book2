package com.ccdipersio;

import java.util.ArrayList;

/**
 * This is the Book class which stores the lists of Students and their Grades.
 * It includes implementation to search the lists and print contents of the lists.
 * Printing can be the entire lists or just a single entry.
 */

public class Book {
    private String bookName;
    private ArrayList<Student> classList = new ArrayList<>();
    private ArrayList<Grades> gradeList = new ArrayList<>();

    /**
     * Constructor.
     * @param bookName  String  Name of the book.
     */
    Book(String bookName) {
        this.bookName = bookName;
    }

    /**
     * Getters.
     */
    ArrayList<Student> getClassList() {
        return classList;
    }
    ArrayList<Grades> getGradeList() {
        return gradeList;
    }

    /**
     * Searches for a Student based on Name.
     * @param studentName   The Name of the Student to be searched.
     * @return  int Index of the Student in the classList; -1 if the Student isn't in the classList.
     */
    int searchForStudent(String studentName) {
        for (int i = 0; i < this.classList.size(); i++) {
            if (this.classList.get(i).getName().equals(studentName))
                return i;
        }
        return -1;
    }

    /**
     * Searches for an Assignment based on Name.
     * @param assignmentName    The Name of the Assignment to be searched.
     * @return  int Index of the Assignment in the gradeList; -1 if the Assignment isn't in the gradeList.
     */
    int searchForAssignmentName(String assignmentName) {
        for (int i = 0; i < this.gradeList.get(0).getGrades().size(); i++) {
            if (this.gradeList.get(0).getGrades().get(i).getName().equals(assignmentName))
                return i;
        }
        return -1;
    }

    /**
     * Searches for an Assignment based on Date.
     * @param assignmentDate    The Date of the Assignment to be searched.
     * @return  int Index of the Assignment in the gradeList; -1 if the Assignment isn't in the gradeList.
     */
    int searchForAssignmentDate(String assignmentDate) {
        for (int i = 0; i < this.gradeList.get(0).getGrades().size(); i++) {
            if (this.gradeList.get(0).getGrades().get(i).getDateOfAssignment().equals(assignmentDate))
                return i;
        }
        return -1;
    }


    /**
     * Prints all contents of the Book.
     */
    void printBook() {
        int longestName = 0;
            for (int i = 1; i < this.classList.size(); i++) {
                if (longestName < this.classList.get(i).getName().length())
                    longestName = this.classList.get(i).getName().length();
            }

        System.out.print("| ");
        for (int i = 0; i < longestName; i++)
            System.out.print(" ");
        System.out.print(" |");
        for (int i = 0; i < this.gradeList.get(0).getGrades().size(); i++) {
            int dateLen = this.gradeList.get(0).getGrades().get(i).getDateOfAssignment().length();
            System.out.print(" " + this.gradeList.get(0).getGrades().get(i).getDateOfAssignment());
            for (int j = 0; j < 9 - dateLen; j++)
                System.out.print(" ");
            System.out.print("|");
        }
        System.out.println();

        for (int i = 1; i < this.gradeList.size(); i++) {
            int studentNameLen = this.classList.get(i).getName().length();
            if (studentNameLen != longestName) {
                System.out.print("| ");
                System.out.print(this.classList.get(i).getName());
                for(int j = 0; j < longestName - studentNameLen; j++)
                    System.out.print(" ");
                System.out.print(" |");
            } else
                System.out.print("| " + this.classList.get(i).getName() + " |");
            functionComponent_printAssignmentsAndGrades(i);
        }
    }

    /**
     * Prints a single Students details.
     * @param studentNumber Number of the Student whose details will be printed.
     */
    void printSingleStudentDetails(int studentNumber) {
        System.out.println("-------------------------------------------------------");
        System.out.println("Name: " + this.classList.get(studentNumber).getName());
        System.out.println("Student Number: " + this.classList.get(studentNumber).getStudentNumber());
        System.out.println("Grade Level " + this.classList.get(studentNumber).getGradeLevel());
        System.out.println("-------------------------------------------------------");
    }

    /**
     * Prints details of the entire class using the printSingleStudentDetails function.
     */
    void printClassDetails() {
        for (int i = 1; i < this.classList.size(); i++)
            this.printSingleStudentDetails(i);
    }

    /**
     * Prints a Student's Assignments and Grades with proper formatting.
     * This function is used in other functions to get proper formatting within those functions.
     * @param studentNumber int Number of Student whose Assignments and Grades will be printed.
     */
    private void functionComponent_printAssignmentsAndGrades(int studentNumber) {
        int dateLength = 10;
        for (int j = 0; j < this.gradeList.get(studentNumber).getGrades().size(); j++) {
            int scoreLen = String.valueOf(this.gradeList.get(studentNumber).getGrades().get(j).getAchievedScore()).length() + 1;
            System.out.print(" " + this.gradeList.get(studentNumber).getGrades().get(j).getAchievedScore());
            for (int i = 0; i < dateLength - scoreLen; i++)
                System.out.print(" ");
            System.out.print("|");
        }

        System.out.println();
    }

    /**
     * Prints a single Student's Assignments and Grades.
     * @param studentNumber int Number of Student whose Assignments and Grade will be printed.
     */
    void printSingleStudentAssignmentsAndGrades(int studentNumber) {
        System.out.print("| ");
        for (int i = 0; i < this.classList.get(studentNumber).getName().length(); i++)
            System.out.print(" ");
        System.out.print(" |");

        for (int i = 0; i < this.gradeList.get(0).getGrades().size(); i++) {
            int dateLen = this.gradeList.get(0).getGrades().get(i).getDateOfAssignment().length();
            System.out.print(" " + this.gradeList.get(0).getGrades().get(i).getDateOfAssignment());
            for (int j = 0; j < 9 - dateLen; j++)
                System.out.print(" ");
            System.out.print("|");
        }
        System.out.println();

        System.out.print("| " + this.classList.get(studentNumber).getName() + " |");
        functionComponent_printAssignmentsAndGrades(studentNumber);

    }

    /**
     * Prints all Assignments' Details
     */
    void printAssignmentDetails() {
        for (int i = 0; i < this.gradeList.get(0).getGrades().size(); i++) {
            this.printSingleAssignmentDetails(i);
        }
    }

    /**
     * Prints a single Assignment's Details
     * @param assignmentIndex   int Index of Assignment in the gradeList to be printed
     */
    void printSingleAssignmentDetails(int assignmentIndex) {
        System.out.println("-------------------------------------------------------");
        System.out.println("Assignment Name: " + this.gradeList.get(0).getGrades().get(assignmentIndex).getName());
        System.out.println("Assignment Type: " + this.gradeList.get(0).getGrades().get(assignmentIndex).getTypeToStringConverted());
        System.out.println("Date of Assignment: " + this.gradeList.get(0).getGrades().get(assignmentIndex).getDateOfAssignment());
        System.out.println("Maximum Score: " + this.gradeList.get(0).getGrades().get(assignmentIndex).getMaxScore());
        System.out.println("-------------------------------------------------------");
    }
}
