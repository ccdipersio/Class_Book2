package com.ccdipersio;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filePath = chooseFile(scanner);
        Book book = new Book("Class Book");

        boolean integrityOK = checkFileIntegrity(filePath);
        if (!integrityOK) {
            System.out.print("File integrity of file at " + filePath + " is bad.\n(1) Reset file to default state or (2) Exit program and correct it yourself? (1 or 2): ");
            int choice = verifyNumericInput(scanner, 1, 2, false);
            if (choice == 1) {
                try {
                    System.out.println("Resetting file to default state...");
                    File toWrite = new File(filePath);
                    FileOutputStream outputStream = new FileOutputStream(toWrite);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    Writer writer = new BufferedWriter(outputStreamWriter);

                    System.out.print("Grade Level: ");
                    int gradeLevel = verifyNumericInput(scanner, 1, 12, true);
                    writer.write("STUDENT,0," + gradeLevel + ",");
                    writer.close();
                } catch (FileNotFoundException f) {
                    System.out.println("File not found...");
                } catch (IOException e) {
                    System.out.println("Problem writing to file...");
                }
            } else {
                System.out.println("Exiting program...");
                return;
            }
        }

        loadRecord(book, filePath);

        printOptions();
        boolean go = true;
        while(go) {
            System.out.print("Command: ");
            int choice = verifyNumericInput(scanner, 1, 10, true);

            switch (choice) {
                case 1:
                    printOptions();
                    break;
                case 2:
                    book.printBook();
                    break;
                case 3:
                    book.printClassDetails();
                    break;
                case 4:
                    printSingleStudentAssignmentsAndGrades(scanner, book);
                    break;
                case 5:
                    printSingleStudentDetails(scanner, book);
                    break;
                case 6:
                    addStudent(scanner, book);
                    break;
                case 7:
                    book.printAssignmentDetails();
                    break;
                case 8:
                    printSingleAssignmentDetails(scanner, book);
                    break;
                case 9:
                    addAssignment(scanner, book);
                    break;
                case 10:
                    saveRecord(book, filePath);
                    go = false;
                    break;
            }
        }
    }

    /**
     * Choose a file or use the default one.
     * @param scanner   Scanner Scanner object from main.
     * @return  String  File path.
     */
    private static String chooseFile(Scanner scanner) {
        String choice = "";
        while (!choice.equals("Y") && !choice.equals("N")) {
            System.out.print("Use custom file? (Y/N): ");
            choice = scanner.next().toUpperCase();
        }

        if (choice.equals("Y")) {
            String filePath;
            boolean doesExist;
            do {
                System.out.print("File Path: ");
                filePath = scanner.next();
                File testFile = new File(filePath);
                doesExist = testFile.exists();
            } while (!doesExist);
            return filePath;
        }
        else
            return "record.txt";
    }

    /**
     * Print possible options to present to user.
     */
    private static void printOptions() {
        System.out.println("Enter the number of the desired action:");
        System.out.println("1: Print Options");
        System.out.println("2: Print Entire Class Book");
        System.out.println("3: Print Entire Class's Details");
        System.out.println("4: Print Single Student's Assignment's And Grades");
        System.out.println("5: Print Single Student's Details");
        System.out.println("6: Add New Student");
        System.out.println("7: Print All Assignment Details");
        System.out.println("8: Print Single Assignment's Details");
        System.out.println("9: Add New Assignment");
        System.out.println("10: Exit");
    }

    /**
     * Loads a file for use in main.
     * @param book  Book    Book created in main.
     * @param filePath  String  Path of file to load.
     */
    private static void loadRecord(Book book, String filePath) {
        try {
            System.out.println("Loading list from " + filePath + "...");
            Scanner fileScanner = new Scanner(new File(filePath));
            String line = fileScanner.nextLine();
            String[]entryLine = line.split(",");
            book.getClassList().add(new Student(entryLine[0].toUpperCase(), Integer.valueOf(entryLine[1]), Integer.valueOf(entryLine[2])));
            book.getGradeList().add(new Grades(book.getClassList().get(0).getStudentNumber()));
            for (int i = 3; i < entryLine.length; i++) {
                book.getGradeList().get(0).getGrades().add(new Assignment(entryLine[i].toUpperCase(), Integer.valueOf(entryLine[i + 1]), entryLine[i + 2], 0, Integer.valueOf(entryLine[i + 3])));
                i += 3;
            }

            int achievedScoreIndex = 6;
            int studentIndex = 1;
            while (fileScanner.hasNext()) {
                line = fileScanner.nextLine();
                entryLine = line.split(",");
                book.getClassList().add(new Student(entryLine[0].toUpperCase(), Integer.valueOf(entryLine[1]), Integer.valueOf(entryLine[2])));
                book.getGradeList().add(new Grades(book.getClassList().get(studentIndex).getStudentNumber()));
                for (int i = 0; i < book.getGradeList().get(0).getGrades().size(); i++) {
                    book.getGradeList().get(studentIndex).getGrades().add(new Assignment(book.getGradeList().get(0).getGrades().get(i).getName(), book.getGradeList().get(0).getGrades().get(i).getType(), book.getGradeList().get(0).getGrades().get(i).getDateOfAssignment(), Integer.valueOf(entryLine[achievedScoreIndex]), book.getGradeList().get(0).getGrades().get(i).getMaxScore()));
                    achievedScoreIndex += 4;
                }
                studentIndex++;
                achievedScoreIndex = 6;
            }
            System.out.println("Record loaded!\n");
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found...");
        }
    }

    /**
     * Checks the file to make sure it's formatting correctly.
     * @param filePath  String Path of file to check.
     * @return  boolean FALSE if file is bad, else TRUE.
     */
    private static boolean checkFileIntegrity(String filePath) {
        System.out.println("\nChecking file integrity at path " + filePath + "...");
        File toCheck = new File(filePath);
        try {
            Scanner scanner = new Scanner(toCheck);
            try {
                String line = scanner.next();
                if (line.indexOf(",") == -1) {
                    scanner.close();
                    return false;
                }
                String[] splitLine = line.split(",");
                int lengthOfLine = splitLine.length;
                if (!splitLine[0].equals("STUDENT")) {
                    scanner.close();
                    return false;
                }
                if (Integer.parseInt(splitLine[1]) != 0) {
                    scanner.close();
                    return false;
                }
                int gradeLevel = Integer.parseInt(splitLine[2]);

                for (int i = 3; i < splitLine.length; i++) {
                    i++;
                    Integer.parseInt(splitLine[i]);
                    i++;
                    if (!verifyDate(splitLine[i])) {
                        scanner.close();
                        return false;
                    }
                    i++;
                    Integer.parseInt(splitLine[i]);
                }
                System.out.println("Line 1 is correct...");

                int studentNumber = 1;
                while (scanner.hasNext()) {
                    line = scanner.next();
                    if (line.indexOf(",") == -1) {
                        scanner.close();
                        return false;
                    }
                    splitLine = line.split(",");
                    if (splitLine.length != lengthOfLine) {
                        scanner.close();
                        return false;
                    }
                    if (Integer.parseInt(splitLine[1]) != studentNumber) {
                        scanner.close();
                        return false;
                    }
                    if (Integer.parseInt(splitLine[2]) != gradeLevel) {
                        scanner.close();
                        return false;
                    }

                    for (int i = 3; i < splitLine.length; i++) {
                        for (int j = 0; j < 3; j++) {
                            if (!splitLine[i].equals("-")) {
                                scanner.close();
                                return false;
                            }
                            i++;
                        }
                        Integer.parseInt(splitLine[i]);
                    }
                    System.out.println("Line " + studentNumber + " is correct...");
                    studentNumber++;
                }
            } catch (NumberFormatException n) {
                scanner.close();
                return false;
            }
        } catch (FileNotFoundException f) {
            System.err.println("File not found...");
        }
        System.out.println();
        return true;
    }

    /**
     * Check to see if the Date String is formatted correctly (MM/DD/YY).
     * @param date  String  Date of the Assignment.
     * @return  boolean False if the Date String is incorrectly formatted, else TRUE
     */
    private static boolean verifyDate(String date) {
        if (date.indexOf("/") == -1)
            return false;
        String[] splitDate = date.split("/");
        try {
            Integer.parseInt(splitDate[0]);
            Integer.parseInt(splitDate[1]);
            Integer.parseInt(splitDate[2]);
        } catch (NumberFormatException n) {
            System.out.println("Date " + date + " is formatted incorrectly...");
            return false;
        }
        return true;
    }

    /**
     * Saves data back to the file.
     * @param book  Book    Book created in main.
     * @param filePath  String  Path of file to write.
     */
    private static void saveRecord(Book book, String filePath) {
        try {
            File toWrite = new File(filePath);
            FileOutputStream outputStream = new FileOutputStream(toWrite);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            Writer writer = new BufferedWriter(outputStreamWriter);

            writer.write(book.getClassList().get(0).getName() + ",0," + book.getClassList().get(0).getGradeLevel() + ",");
            for (int i = 0; i < book.getGradeList().get(0).getGrades().size(); i++) {
                writer.write(book.getGradeList().get(0).getGrades().get(i).getName() + ",");
                writer.write(book.getGradeList().get(0).getGrades().get(i).getType() + ",");
                writer.write(book.getGradeList().get(0).getGrades().get(i).getDateOfAssignment() + ",");
                writer.write(book.getGradeList().get(0).getGrades().get(i).getMaxScore() + ",");
            }
            writer.write("\n");

            for (int i = 1; i < book.getClassList().size(); i++) {
                writer.write(book.getClassList().get(i).getName() + ",");
                writer.write(book.getClassList().get(i).getStudentNumber() + ",");
                writer.write(book.getClassList().get(i).getGradeLevel() + ",");
                for (int j = 0; j < book.getGradeList().get(0).getGrades().size(); j++)
                    writer.write("-,-,-," + book.getGradeList().get(i).getGrades().get(j).getAchievedScore() + ",");
                writer.write("\n");
            }
            writer.close();
        } catch (FileNotFoundException f) {
            System.err.println("File not found...");
        } catch (IOException e) {
            System.err.println("Problem writing to file...");
        }
    }

    /**
     * Outer controller for printSingleStudentAssignmentsAndGrades from within the Book class.
     * Deals with getting input and connects the searching functions to the printer.
     * @param scanner   Scanner Scanner object from main.
     * @param book  Book    Book created in main.
     */
    private static void printSingleStudentAssignmentsAndGrades(Scanner scanner, Book book) {
        System.out.print("Search for student by (1) Student's Name or (2) Student's Number? (1 or 2): ");
        int choice = verifyNumericInput(scanner, 1, 2, false);

        switch (choice) {
            case 1:
                System.out.print("Student's Name: ");
                int index = book.searchForStudent(scanner.next().toUpperCase());
                if (index == -1) {
                    System.out.println("Student doesn't exist in book...");
                    return;
                } else
                    book.printSingleStudentAssignmentsAndGrades(index);
                break;
            case 2:
                System.out.print("Student's Number: ");
                book.printSingleStudentAssignmentsAndGrades(verifyNumericInput(scanner, 1, book.getClassList().size() - 1, true));
        }
    }

    /**
     * Outer controller for printSingleStudentDetails from within the Book class.
     * Deals with getting input and connects the searching functions to the printer.
     * @param scanner   Scanner Scanner object from main.
     * @param book  Book    Book created in main.
     */
    private static void printSingleStudentDetails(Scanner scanner, Book book) {
        System.out.print("Search by (1) Student's Name or (2) Student's Number? (1 or 2): ");
        int choice = verifyNumericInput(scanner, 1, 2, false);

        switch (choice) {
            case 1:
                System.out.print("Student's Name: ");
                int index = book.searchForStudent(scanner.next().toUpperCase());
                if (index == -1) {
                    System.out.println("Student doesn't exist in book...");
                    return;
                } else
                    book.printSingleStudentDetails(index);
                break;
            case 2:
                System.out.print("Student's Number: ");
                book.printSingleStudentDetails(verifyNumericInput(scanner, 1, book.getClassList().size() - 1, true));
        }
    }

    /**
     * Adds a Student to the Book.
     * @param scanner   Scanner Scanner object from main.
     * @param book  Book    Book created in main.
     */
    private static void addStudent(Scanner scanner, Book book) {
        int nextIndex = book.getClassList().size();

        System.out.print("Student's Name: ");
        String studentName = scanner.next().toUpperCase();

        book.getClassList().add(new Student(studentName, nextIndex, book.getClassList().get(0).getGradeLevel()));

        book.getGradeList().add(new Grades(nextIndex));

        int studentAchievedScore;
        for (int i = 0; i < book.getGradeList().get(0).getGrades().size(); i++) {
            System.out.print(studentName + "'s Achieved Score for " + book.getGradeList().get(0).getGrades().get(i).getName() + " on " + book.getGradeList().get(0).getGrades().get(i).getDateOfAssignment() + ": ");
            studentAchievedScore = verifyNumericInput(scanner, 0, book.getGradeList().get(0).getGrades().get(i).getMaxScore(), true);
            book.getGradeList().get(nextIndex).getGrades().add(new Assignment(book.getGradeList().get(0).getGrades().get(i).getName(), book.getGradeList().get(0).getGrades().get(i).getType(), book.getGradeList().get(0).getGrades().get(i).getDateOfAssignment(), studentAchievedScore, book.getGradeList().get(0).getGrades().get(i).getMaxScore()));
        }
        System.out.println(studentName + " added!");
    }

    /**
     * Outer Controller for printSingleAssignmentDetails from within the Book class.
     * Deals with getting input and connects the searching functions to the printer.
     * @param scanner   Scanner Scanner object from main.
     * @param book  Book    Book created in main.
     */
    private static void printSingleAssignmentDetails(Scanner scanner, Book book) {
        System.out.print("Search by (1) Assignment's Name or (2) Date of Assignment? (1 or 2): ");
        int choice = verifyNumericInput(scanner, 1, 2, false);

        switch (choice) {
            case 1:
                System.out.print("Assignment's Name: ");
                int index = book.searchForAssignmentName(scanner.next().toUpperCase());
                if (index == -1) {
                    System.out.println("Assignment doesn't exist in book...");
                    return;
                } else
                    book.printSingleAssignmentDetails(index);
                break;
            case 2:
                String assignmentDate = produceDate(scanner);
                index = book.searchForAssignmentDate(assignmentDate);
                if (index == -1) {
                    System.out.println("Assignment with date " + assignmentDate + " doesn't exist in book...");
                    return;
                } else
                    book.printSingleAssignmentDetails(index);
                break;
        }
    }

    /**
     * Adds an Assignment to the Book.
     * @param scanner   Scanner Scanner object from main.
     * @param book  Book    Book created in main.
     */
    private static void addAssignment(Scanner scanner, Book book) {
        boolean nameClear = false;
        String assignmentName;
        do {
            System.out.print("Assignment's Name: ");
            assignmentName = scanner.next().toUpperCase();
            for (int i = 0; i < book.getGradeList().get(0).getGrades().size(); i++) {
                if (book.getGradeList().get(0).getGrades().get(i).getName().equals(assignmentName)) {
                    System.out.println("Name already exists...");
                    nameClear = false;
                } else
                    nameClear = true;
            }
        } while (!nameClear);

        System.out.println();

        System.out.print("0 - Final Exam\n1 - Midterm Exam\n2 - Test\n3 - Quiz\n4 - Essay\n5 - Presentation\n6 - Other Project\n7 - Homework\n8 - Classwork\n9 - Participation\nAssignment Type (0-9): ");
        int assignmentType = verifyNumericInput(scanner, 0, 9, true);

        System.out.println();

        String assignmentDate = produceDate(scanner);

        System.out.println();

        System.out.print("Maximum Possible Score: ");
        int assignmentMaxScore = verifyNumericInput(scanner, 0, 1000, true);

        book.getGradeList().get(0).getGrades().add(new Assignment(assignmentName, assignmentType, assignmentDate, 0, assignmentMaxScore));

        int assignmentIndex = book.getGradeList().get(0).getGrades().size() - 1;

        int assignmentAchievedScore;
        for (int i = 1; i < book.getClassList().size(); i++) {
            System.out.print(book.getClassList().get(i).getName() + "'s Achieved Score: ");
            assignmentAchievedScore = verifyNumericInput(scanner, 0, book.getGradeList().get(0).getGrades().get(assignmentIndex).getMaxScore(), true);
            book.getGradeList().get(i).getGrades().add(new Assignment(assignmentName, assignmentType, assignmentDate, assignmentAchievedScore, assignmentMaxScore));
            System.out.println();
        }
        System.out.println(assignmentName + " added!");
    }

    /**
     * Checks numeric input for proper format and range.
     * @param scanner   Scanner Scanner object from main.
     * @param lowEnd    int Low end of desired range.
     * @param highEnd   int High end of desired range.
     * @param hasRange  boolean TRUE if lowEnd to highEnd is a range of numbers; else, FALSE.
     * @return  int Desired value.
     */
    private static int verifyNumericInput(Scanner scanner, int lowEnd, int highEnd, boolean hasRange) {
        boolean isNumber = false;
        String input;
        int choice = -1;
        do {
            try {
                input = scanner.next();
                choice = Integer.parseInt(input);
                if (hasRange) {
                    if (choice < lowEnd || choice > highEnd) {
                        System.out.println("Number must be between " + lowEnd + " and " + highEnd + ".");
                        continue;
                    }
                } else {
                    if (choice != lowEnd && choice != highEnd) {
                        System.out.println("Number must be " + lowEnd + " or " + highEnd + ".");
                        continue;
                    }
                }
                isNumber = true;
            } catch (NumberFormatException e) {
                System.out.println("Input not valid!");
            }
        } while (!isNumber);
        return choice;
    }

    /**
     * Guides user to create a valid Date String.
     * @param scanner   Scanner Scanner object from main.
     * @return  String  Correctly formatted Date String.
     */
    private static String produceDate(Scanner scanner) {
        System.out.print("1 - January\n2 - February\n3 - March\n4 - April\n5 - May\n6 - June\n7 - July\n8 - August\n9 - September\n10 - October\n11 - November\n12 - December\nMonth of Assignment (1 - 12): ");  // GET MONTH
        int assignmentDateMonth = verifyNumericInput(scanner, 1, 12, true);
        System.out.print("Day of Assignment (0 - 31): ");  // GET DAY
        int assignmentDateDay = verifyNumericInput(scanner, 1, 31, true);
        System.out.print("Year of Assignment: ");  // GET YEAR
        int assignmentDateYear = verifyNumericInput(scanner, 0, 99, true);
        return assignmentDateMonth + "/" + assignmentDateDay + "/" + assignmentDateYear;  // RETURN DATE STRING
    }
}
