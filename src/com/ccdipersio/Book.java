package com.ccdipersio;

import java.util.ArrayList;

public class Book {
    private String bookName;  // NAME OF BOOK
    private ArrayList<Student> classList = new ArrayList<>();  // LIST OF STUDENTS EXCLUSIVELY
    private ArrayList<Grades> gradeList = new ArrayList<>();  // LIST OF GRADES EXCLUSIVELY


    // CONSTRUCTOR
    Book(String bookName) {
        this.bookName = bookName;
    }


    // GETTERS
    String getBookName() {
        return bookName;
    }
    ArrayList<Student> getClassList() {
        return classList;
    }
    ArrayList<Grades> getGradeList() {
        return gradeList;
    }


    // SETTERS
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public void setClassList(ArrayList<Student> classList) {
        this.classList = classList;
    }
    public void setGradeList(ArrayList<Grades> gradeList) {
        this.gradeList = gradeList;
    }


    // SEARCHING FUNCTIONS
    // SEARCH FOR STUDENT BY NAME STRING
    int searchForStudent(String studentName) {
        for (int i = 0; i < this.classList.size(); i++) {
            if (this.classList.get(i).getName().equals(studentName))
                return i;
        }
        return -1;
    }
    // SEARCH FOR ASSIGNMENT BY NAME STRING
    int searchForAssignmentName(String assignmentName) {
        for (int i = 0; i < this.gradeList.get(0).getGrades().size(); i++) {
            if (this.gradeList.get(0).getGrades().get(i).getName().equals(assignmentName))
                return i;
        }
        return -1;
    }
    // SEARCH FOR ASSIGNMENT BY DATE STRING
    int searchForAssignmentDate(String assignmentDate) {
        for (int i = 0; i < this.gradeList.get(0).getGrades().size(); i++) {
            if (this.gradeList.get(0).getGrades().get(i).getDateOfAssignment().equals(assignmentDate))
                return i;
        }
        return -1;
    }

    // OTHER FUNCTIONS
    // PRINT ENTIRE BOOK
    void printBook() {
        int longestName = 0;  // VARIABLE TO STORE LENGTH OF LONGEST NAME FOR FORMATTING OUTPUT
            for (int i = 1; i < this.classList.size(); i++) {  // ITERATE THROUGH CLASS LIST
                if (longestName < this.classList.get(i).getName().length())  // CHECK CURRENT LENGTH AGAINST I'TH STUDENT'S NAME'S LENGTH
                    longestName = this.classList.get(i).getName().length();  // IF SHORTER, REASSIGN TO I'TH STUDENT'S NAME'S LENGTH
            }

        // PRINT FIRST LINE (DATES)
        System.out.print("| ");  // PRINT NAME SLOT (EMPTY IN FIRST (DATE) LINE
        for (int i = 0; i < longestName; i++)
            System.out.print(" ");
        System.out.print(" |");
        for (int i = 0; i < this.gradeList.get(0).getGrades().size(); i++)  // PRINT SLOTS WITH DATES
            System.out.print(" " + this.gradeList.get(0).getGrades().get(i).getDateOfAssignment() + " |");
        System.out.println();

        // PRINT SUBSEQUENT LINES
        int studentNameLen = 0;  // VARIABLE FOR LENGTH OF I'TH STUDENT'S NAME

        // PRINT STUDENT'S NAME
        for (int i = 1; i < this.gradeList.size(); i++) {  // ITERATE THROUGH GRADE LIST
            studentNameLen = this.classList.get(i).getName().length();  // GET LENGTH OF I'TH STUDENT'S NAME
            if (studentNameLen != longestName) {  // CHECK IF I'TH STUDENT'S NAME ISN'T THE LONGEST
                System.out.print("| ");  // PRINT LEFT-MOST BAR
                for (int j = 0; j < ((longestName - studentNameLen) / 2); j++)  // PRINT SPACES
                    System.out.print(" ");
                System.out.print(this.classList.get(i).getName());  // PRINT I'TH STUDENT'S NAME
                for (int j = 0; j < ((longestName - studentNameLen) / 2) + 1; j++)  // PRINT SPACES
                    System.out.print(" ");
                if (longestName % 2 == 0)  // PRINT SPACE AND BAR IF NAME HAS EVEN LENGTH
                    System.out.print(" |");
                else  // ELSE JUST PRINT BAR
                    System.out.print("|");
            } else  // IF LONGEST NAME, JUST PRINT NAME
                System.out.print("| " + this.classList.get(i).getName() + " |");
            // PRINT STUDENT'S ASSIGNMENTS' GRADES
            functionComponent_printAssignmentsAndGrades(i);
        }
    }
    // PRINT SINGLE STUDENT'S DETAILS
    void printSingleStudentDetails(int studentNumber) {
        System.out.println("-------------------------------------------------------");
        System.out.println("Name: " + this.classList.get(studentNumber).getName());
        System.out.println("Student Number: " + this.classList.get(studentNumber).getStudentNumber());
        System.out.println("Grade Level " + this.classList.get(studentNumber).getGradeLevel());
        System.out.println("-------------------------------------------------------");
    }
    // PRINT ENTIRE CLASS'S DETAILS
    void printClassDetails() {
        for (int i = 1; i < this.classList.size(); i++)
            this.printSingleStudentDetails(i);
    }
    // INTERNAL FUNCTION COMPONENT FOR PRINTING ASSIGNMENT DETAILS
    private void functionComponent_printAssignmentsAndGrades(int studentNumber) {
        int dateLength = 0;
        for (int j = 0; j < this.gradeList.get(studentNumber).getGrades().size(); j++) {  // ITERATE THROUGH I'TH STUDENT'S GRADES
            dateLength = this.gradeList.get(studentNumber).getGrades().get(j).getDateOfAssignment().length();  // GET LENGTH OF J'TH ASSIGNMENT'S STRING
            System.out.print(" ");  // PRINT SPACE
            if (this.gradeList.get(studentNumber).getGrades().get(j).getAchievedScore() > -1 && this.gradeList.get(studentNumber).getGrades().get(j).getAchievedScore() < 10) {  // IF SCORE IS BETWEEN 0 AND 9
                for (int k = 0; k < ((dateLength - 1) / 2); k++)  // PRINT SPACES               }
                    System.out.print(" ");
                System.out.print(this.gradeList.get(studentNumber).getGrades().get(j).getAchievedScore());  // PRINT SCORE
                for (int k = 0; k < ((dateLength - 1) / 2) + 1; k++)  // PRINT SPACES
                    System.out.print(" ");
            } else if(this.gradeList.get(studentNumber).getGrades().get(j).getAchievedScore() > 9 && this.gradeList.get(studentNumber).getGrades().get(j).getAchievedScore() < 100) {  // IF SCORE IS BETWEEN 10 AND 99
                for (int k = 0; k < ((dateLength - 2) / 2); k++)  // PRINT SPACES
                    System.out.print(" ");
                System.out.print(this.gradeList.get(studentNumber).getGrades().get(j).getAchievedScore());  // PRINT SCORE
                for (int k = 0; k < ((dateLength - 2) / 2) + 1; k++)  // PRINT SPACES
                    System.out.print(" ");
            } else if (this.gradeList.get(studentNumber).getGrades().get(j).getAchievedScore() > 99 && this.gradeList.get(studentNumber).getGrades().get(j).getAchievedScore() < 1000) {  // IF SCORE IS BETWEEN 100 AND 999
                for (int k = 0; k < ((dateLength - 3) / 2); k++)  // PRINT SPACES
                    System.out.print(" ");
                System.out.print(this.gradeList.get(studentNumber).getGrades().get(j).getAchievedScore());  // PRINT SCORE
                for (int k = 0; k < ((dateLength - 2) / 2) + 1; k++)  // PRINT SPACES
                    System.out.print(" ");
            }
            if(String.valueOf(this.gradeList.get(studentNumber).getGrades().get(j).getAchievedScore()).length() % 2 == 0)  // PRINT SPACE AND BAR IF NAME HAS EVEN LENGTH
                System.out.print(" |");
            else  // ELSE JUST PRINT BAR
                System.out.print("|");
        }
        System.out.println();  // PRINT NEW LINE
    }
    // PRINT SINGLE STUDENT'S ASSIGNMENTS AND GRADES
    void printSingleStudentAssignmentsAndGrades(int studentNumber) {
        // PRINT FIRST LINE (DATES)
        System.out.print("| ");  // PRINT NAME SLOT (EMPTY IN FIRST LINE)
        for (int i = 0; i < this.classList.get(studentNumber).getName().length(); i++)  // PRINT SPACES
            System.out.print(" ");
        System.out.print(" |");
        for (int i = 0; i < this.gradeList.get(0).getGrades().size(); i++)  // PRINT DATES
            System.out.print(" " + this.gradeList.get(0).getGrades().get(i).getDateOfAssignment() + " |");
        System.out.println("");

        System.out.printf("| " + this.classList.get(studentNumber).getName() + " |"); // PRINT NAME
        // PRINT ASSIGNMENTS' GRADES
        functionComponent_printAssignmentsAndGrades(studentNumber);

    }
    // PRINT ALL ASSIGNMENT DETAILS
    void printAssignmentDetails() {
        for (int i = 0; i < this.gradeList.get(0).getGrades().size(); i++) {
            System.out.println("-------------------------------------------------------");
            System.out.println("Assignment Name: " + this.gradeList.get(0).getGrades().get(i).getName());
            System.out.println("Assignment Type: " + this.gradeList.get(0).getGrades().get(i).getTypeToStringConverted());
            System.out.println("Date of Assignment: " + this.gradeList.get(0).getGrades().get(i).getDateOfAssignment());
            System.out.println("Maximum Score: " + this.gradeList.get(0).getGrades().get(i).getMaxScore());
            System.out.println("-------------------------------------------------------");
        }
    }
    // PRINT SINGLE ASSIGNMENT'S DETAILS
    void printSingleAssignmentDetails(int assignmentIndex) {
        System.out.println("-------------------------------------------------------");
        System.out.println("Assignment Name: " + this.gradeList.get(0).getGrades().get(assignmentIndex).getName());
        System.out.println("Assignment Type: " + this.gradeList.get(0).getGrades().get(assignmentIndex).getTypeToStringConverted());
        System.out.println("Date of Assignment: " + this.gradeList.get(0).getGrades().get(assignmentIndex).getDateOfAssignment());
        System.out.println("Maximum Score: " + this.gradeList.get(0).getGrades().get(assignmentIndex).getMaxScore());
        System.out.println("-------------------------------------------------------");
    }
}
