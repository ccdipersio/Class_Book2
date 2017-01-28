package com.ccdipersio;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filePath = chooseFile(scanner);  // GET FILE PATH STRING
        Book book = new Book("Class Book");  // CREATE MAIN BOOK OBJECT

        boolean integrityOK = checkFileIntegrity(filePath);
        if (!integrityOK) {
            System.out.print("File integrity of file at " + filePath + " is bad.\n(1) Reset file to default state or (2) Exit program and correct it yourself? (1 or 2): ");  // GET USER CHOICE
            int choice = verifyNumericInput(scanner, 1, 2, false);
            if (choice == 1) {
                try {
                    System.out.println("Resetting file to default state...");
                    File toWrite = new File(filePath);  // CREATE FILE OBJECT BASED ON FILE PATH AND VARIOUS OTHER OBJECTS FOR THE WRITER
                    FileOutputStream outputStream = new FileOutputStream(toWrite);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    Writer writer = new BufferedWriter(outputStreamWriter);

                    System.out.print("Grade Level: ");  // GET GRADE LEVEL FOR FILE
                    int gradeLevel = verifyNumericInput(scanner, 1, 12, true);
                    writer.write("STUDENT,0," + gradeLevel + ",");  // WRITE DEFAULT STATE TO FILE
                    writer.close();  // CLOSE WRITER
                } catch (FileNotFoundException f) {  // IF CAN'T FILE FILE
                    System.out.println("File not found...");
                } catch (IOException e) {  // IF CAN'T WRITE TO FILE
                    System.out.println("Problem writing to file...");
                }
            } else {
                System.out.println("Exiting program...");
                return;
            }
        }

        loadRecord(book, filePath);  // POPULATE BOOK BASED ON FILE DEFINED IN FILE PATH STRING

        printOptions();
        boolean go = true;
        while(go) {
            int choice = verifyNumericInput(scanner, 1, 10, true);  // GET USER CHOICE

            switch (choice) {
                case 1:  // PRINT OPTIONS
                    printOptions();
                    break;
                case 2:  // PRINT ENTIRE CLASS BOOK
                    book.printBook();
                    break;
                case 3:  // PRINT ENTIRE CLASS'S DETAILS
                    book.printClassDetails();
                    break;
                case 4:  // PRINT SINGLE STUDENT'S ASSIGNMENTS AND GRADES
                    printSingleStudentAssignmentsAndGrades(scanner, book);
                    break;
                case 5:  // PRINT SINGLE STUDENT'S DETAILS
                    printSingleStudentDetails(scanner, book);
                    break;
                case 6:  // ADD NEW STUDENT
                    addStudent(scanner, book);
                    break;
                case 7:  // PRINT ALL ASSIGNMENT DETAILS
                    book.printAssignmentDetails();
                    break;
                case 8:  // PRINT SINGLE ASSIGNMENT'S DETAILS
                    printSingleAssignmentDetails(scanner, book);
                    break;
                case 9:  // ADD NEW ASSIGNMENT
                    addAssignment(scanner, book);
                    break;
                case 10:  // EXIT
                    saveRecord(book, filePath);
                    go = false;
                    break;
            }
        }
    }

    // CHOOSE FILE
    private static String chooseFile(Scanner scanner) {
        String choice = "";  // INITIALIZE STRING FOR CHOICE
        while (!choice.equals("Y") && !choice.equals("N")) {  // CHECK FOR CORRECT VALUES
            System.out.print("Use custom file? (Y/N): ");  // ASK FOR VALUE
            choice = scanner.next().toUpperCase();  // GET VALUE
        }

        if (choice.equals("Y")) {
            String filePath;  // DECLARE NEW STRING FOR FILE PATH
            boolean doesExist;  // DECLARE BOOL FOR CHECKER
            do {
                System.out.print("File Path: ");  // ASK FOR FILE PATH
                filePath = scanner.next();  // GET FILE PATH
                File testFile = new File(filePath);  // CREATE TEMP FILE USING FILE PATH
                doesExist = testFile.exists();  // CHECK IF TEMP FILE EXISTS
            } while (!doesExist);  // LOOP WHILE TEMP FILE DOESN'T EXIST
            return filePath;  // RETURN FILE PATH
        }
        else
            return "record.txt";  // ELSE RETURN DEFAULT RECORD FILE
    }

    // PRINT OPTIONS
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

    // LOAD RECORD FILE
    private static void loadRecord(Book book, String filePath) {
        try {
            System.out.println("Loading list from " + filePath + "...");  // ANNOUNCE WHERE LOADING LIST FROM
            Scanner fileScanner = new Scanner(new File(filePath));  // CREATE AND INITIALIZE SCANNER
            //fileScanner.useDelimiter(",");  // SET DELIMITER FOR VALUES SEPARATED IN RECORD FILE
            String line = fileScanner.nextLine();  // STORE FIRST LINE
            String[]entryLine = line.split(",");  // SPLIT FIRST LINE
            book.getClassList().add(new Student(entryLine[0].toUpperCase(), Integer.valueOf(entryLine[1]), Integer.valueOf(entryLine[2])));  // CREATE DUMMY STUDENT FROM FIRST 3 VALUES OF SEPARATED FIRST LINE
            book.getGradeList().add(new Grades(book.getClassList().get(0).getStudentNumber()));
            for (int i = 3; i < entryLine.length; i++) {  // ITERATE UNTIL LENGTH OF SEPARATED ENTRY LINE IS HIT
                book.getGradeList().get(0).getGrades().add(new Assignment(entryLine[i].toUpperCase(), Integer.valueOf(entryLine[i + 1]), entryLine[i + 2], 0, Integer.valueOf(entryLine[i + 3])));  // ADD ASSIGNMENTS TO DUMMY STUDENT
                i += 3;  // INCREASE ITERATOR
            }

            int achievedScoreIndex = 6;  // CREATE VARIABLE TO MARK WHERE CURRENT ACHIEVED SCORE VALUE WILL BE IN LINE
            int studentIndex = 1;  // SET STUDENT INDEX TO 1
            while (fileScanner.hasNext()) {  // ITERATE WHILE NOT EOF
                line = fileScanner.nextLine();  // GET LINE
                entryLine = line.split(",");  // SPLIT LINE
                book.getClassList().add(new Student(entryLine[0].toUpperCase(), Integer.valueOf(entryLine[1]), Integer.valueOf(entryLine[2])));  // ADD NEW STUDENT IN CURRENT LINE
                book.getGradeList().add(new Grades(book.getClassList().get(studentIndex).getStudentNumber()));  // OPEN NEW GRADES ENTRY FOR NEW STUDENT
                for (int i = 0; i < book.getGradeList().get(0).getGrades().size(); i++) {  // ITERATE THROUGH SIZE OF DUMMY STUDENT'S GRADE LIST
                    book.getGradeList().get(studentIndex).getGrades().add(new Assignment(book.getGradeList().get(0).getGrades().get(i).getName(), book.getGradeList().get(0).getGrades().get(i).getType(), book.getGradeList().get(0).getGrades().get(i).getDateOfAssignment(), Integer.valueOf(entryLine[achievedScoreIndex]), book.getGradeList().get(0).getGrades().get(i).getMaxScore()));  // ADD ASSIGNMENT
                    achievedScoreIndex += 4;  // INCREASE ACHIEVED SCORE MARKER BY 4
                }
                studentIndex++;  // INCREASE STUDENT INDEX BY 1
                achievedScoreIndex = 6;  // RESET ACHIEVED SCORE MARKER
            }
            System.out.println("Record loaded!\n");  // ANNOUNCE FILE LOADED
        } catch (FileNotFoundException e) {  // HANDLE ERROR -> CAN'T FIND FILE
            System.err.println("File not found...");
        }
    }

    // CHECK FILE INTEGRITY
    private static boolean checkFileIntegrity(String filePath) {
        System.out.println("\nChecking file integrity at path " + filePath + "...");
        File toCheck = new File(filePath);  // CREATE FILE OBJECT WITH PATH

        try {
            Scanner scanner = new Scanner(toCheck);  // SCANNER FOR FILE
            String line = scanner.next();  // GET FIRST LINE
            if (line.indexOf(",") == -1)  // CHECK FOR COMMA IN STRING
                return false;
            String[] splitLine = line.split(",");  // SPLIT ALONG COMMA
            int lengthOfLine = splitLine.length;  // GET LENGTH OF TOP LINE
            if (!splitLine[0].equals("STUDENT")) {  // CHECK FOR DUMMY STUDENT NAME
                return false;
            }
            if (Integer.parseInt(splitLine[1]) != 0) {  // CHECK FOR DUMMY STUDENT NUMBER
                return false;
            }
            int gradeLevel = Integer.parseInt(splitLine[2]);  // CHECK IF DUMMY STUDENT GRADE LEVEL IS NUMBER

            for (int i = 3; i < splitLine.length; i++) {  // CHECK ASSIGNMENT ENTRIES FOR DUMMY STUDENT
                i++;
                Integer.parseInt(splitLine[i]);  // TYPE
                i++;
                if (!verifyDate(splitLine[i]))  // DATE
                    return false;
                i++;
                Integer.parseInt(splitLine[i]);  // MAZ SCORE
            }
            System.out.println("Line 1 is correct...");  // ANNOUNCE FIRST LINE IS CORRECT

            int studentNumber = 1;  // STUDENT NUMBER VARIABLE TO CHECK ENTRIES
            while(scanner.hasNext()) {
                line = scanner.next();  // GET LINE
                if (line.indexOf(",") == -1)  // CHECK FOR COMMA IN STRING
                    return false;
                splitLine = line.split(",");  // SPLIT ALONG COMMA
                if (splitLine.length != lengthOfLine) {  // CHECK LENGTH OF LINE AGAINST LENGTH OF STOP LINE
                    return false;
                }
                if (Integer.parseInt(splitLine[1]) != studentNumber) {  // CHECK STUDENT NUMBER
                    return false;
                }
                if (Integer.parseInt(splitLine[2]) != gradeLevel) {  // CHECK GRADE LEVEL
                    return false;
                }

                for (int i = 3; i < splitLine.length; i++) {  // CHECK STUDENT'S ASSIGNMENT ENTRIES
                    for (int j = 0; j < 3; j++) {
                        if (!splitLine[i].equals("-"))  // CHECK FIRTH THREE ENTRIES FOR STUDENT'S ASSIGNMENTS
                            return false;
                        i++;
                    }
                    Integer.parseInt(splitLine[i]);  // CHECK IF SCORE IS NUMBER
                }
                System.out.println("Line " + studentNumber + " is correct...");  // ANNOUNCE LINE IS OK
                studentNumber++;  // INCREMENT STUDENT NUMBER
            }
        } catch (FileNotFoundException f) {  // CAN'T FIND FILE
            System.err.println("File not found...");
        } catch (NumberFormatException n) {  // NUMBER FORMAT IS BAD
            return false;
        }
        System.out.println();
        return true;
    }

    // VERIFY DATE STRUCTURE
    private static boolean verifyDate(String date) {
        if (date.indexOf("/") == -1)  // IF DELIMITER ISN'T IN STRING
            return false;
        String[] splitDate = date.split("/");  // SPLIT DATE
        try {
            Integer.parseInt(splitDate[0]);  // CHECK MONTH
            Integer.parseInt(splitDate[1]);  // CHECK DAY
            Integer.parseInt(splitDate[2]);  // CHECK YEAR
        } catch (NumberFormatException n) {  // ISN'T NUMBER
            System.out.println("Date " + date + " is formatted incorrectly...");
            return false;
        }
        return true;
    }

    // SAVE RECORD FILE
    private static void saveRecord(Book book, String filePath) {
        try {
            File toWrite = new File(filePath);  // CREATE FILE OBJECT BASED ON FILE PATH AND VARIOUS OTHER OBJECTS FOR THE WRITER
            FileOutputStream outputStream = new FileOutputStream(toWrite);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            Writer writer = new BufferedWriter(outputStreamWriter);

            writer.write(book.getClassList().get(0).getName() + ",0," + book.getClassList().get(0).getGradeLevel() + ",");  // WRITE DUMMY STUDENT'S NAME, NUMBER, AND GRADE LEVEL TO FILE
            for (int i = 0; i < book.getGradeList().get(0).getGrades().size(); i++) {
                writer.write(book.getGradeList().get(0).getGrades().get(i).getName() + ",");  // WRITE NAME OF I'TH ASSIGNMENT
                writer.write(book.getGradeList().get(0).getGrades().get(i).getType() + ",");  // WRITE TYPE OF I'TH ASSIGNMENT
                writer.write(book.getGradeList().get(0).getGrades().get(i).getDateOfAssignment() + ",");  // WRITE DATE OF I'TH ASSIGNMENT
                writer.write(book.getGradeList().get(0).getGrades().get(i).getMaxScore() + ",");  // WRITE MAX SCORE OF I'TH ASSIGNMENT
            }
            writer.write("\n");  // WRITE NEW LINE

            for (int i = 1; i < book.getClassList().size(); i++) {
                writer.write(book.getClassList().get(i).getName() + ",");  // WRITE I'TH STUDENT'S NAME
                writer.write(book.getClassList().get(i).getStudentNumber() + ",");  // WRITE I'TH STUDENT'S NUMBER
                writer.write(book.getClassList().get(i).getGradeLevel() + ",");  // WRITE I'TH STUDENT'S GRADE LEVEL
                for (int j = 0; j < book.getGradeList().get(0).getGrades().size(); j++)
                    writer.write("-,-,-," + book.getGradeList().get(i).getGrades().get(j).getAchievedScore() + ",");  // WRITE I'TH STUDENT'S J'TH SCORE
                writer.write("\n");  // WRITE NEW LINE
            }
            writer.close();  // CLOSE WRITER
        } catch (FileNotFoundException f) {  // IF FILE NOT FOUND
            System.err.println("File not found...");
        } catch (IOException e) {  // IF CAN'T WRITE TO FILE
            System.err.println("Problem writing to file...");
        }
    }

    // PRINT SINGLE STUDENT'S ASSIGNMENTS AND GRADES
    private static void printSingleStudentAssignmentsAndGrades(Scanner scanner, Book book) {
        System.out.print("Search for student by (1) Student's Name or (2) Student's Number? (1 or 2): ");  // GET USER CHOICE
        int choice = verifyNumericInput(scanner, 1, 2, false);

        switch (choice) {
            case 1:  // IF SEARCH BY NAME
                System.out.print("Student's Name: ");  // GET NAME
                int index = book.searchForStudent(scanner.next().toUpperCase());
                if (index == -1) {  // CHECK IF NAME IS IN BOOK
                    System.out.println("Student doesn't exist in book...");
                    return;
                } else
                    book.printSingleStudentAssignmentsAndGrades(index);  // PRESENT DATA
                break;
            case 2:  // IF NUMBER
                System.out.print("Student's Number: ");  // GET NUMBER AND PRESENT DATA
                book.printSingleStudentAssignmentsAndGrades(verifyNumericInput(scanner, 1, book.getClassList().size() - 1, true));
        }
    }

    // PRINT SINGLE STUDENT'S DETAILS -- OUTER CONTROLLER
    private static void printSingleStudentDetails(Scanner scanner, Book book) {
        System.out.print("Search by (1) Student's Name or (2) Student's Number? (1 or 2): ");  // GET USER CHOICE
        int choice = verifyNumericInput(scanner, 1, 2, false);

        switch (choice) {
            case 1:  // IF SEARCH BY NAME
                System.out.print("Student's Name: ");  // GET NAME
                int index = book.searchForStudent(scanner.next().toUpperCase());
                if (index == -1) {  // CHECK IF NAME IS IN BOOK
                    System.out.println("Student doesn't exist in book...");
                    return;
                } else
                    book.printSingleStudentDetails(index);  // PRESENT DATA
                break;
            case 2:  // IF NUMBER
                System.out.print("Student's Number: ");  // GET NUMBER AND PRESENT DATA
                book.printSingleStudentDetails(verifyNumericInput(scanner, 1, book.getClassList().size() - 1, true));
        }
    }

    // ADD STUDENT TO BOOK
    private static void addStudent(Scanner scanner, Book book) {
        int nextIndex = book.getClassList().size();  // FIND INTEGER FOR NEXT STUDENT'S NUMBER

        System.out.print("Student's Name: ");  // GET NEXT STUDENT'S NAME
        String studentName = scanner.next().toUpperCase();

        book.getClassList().add(new Student(studentName, nextIndex, book.getClassList().get(0).getGradeLevel()));  // ADD STUDENT TO CLASS LIST

        book.getGradeList().add(new Grades(nextIndex));  // ADD STUDENT TO GRADES LIST
        // ADD GRADES
        int studentAchievedScore;
        for (int i = 0; i < book.getGradeList().get(0).getGrades().size(); i++) {
            System.out.print(studentName + "'s Achieved Score for " + book.getGradeList().get(0).getGrades().get(i).getName() + " on " + book.getGradeList().get(0).getGrades().get(i).getDateOfAssignment() + ": ");
            studentAchievedScore = verifyNumericInput(scanner, 0, book.getGradeList().get(0).getGrades().get(i).getMaxScore(), true);
            book.getGradeList().get(nextIndex).getGrades().add(new Assignment(book.getGradeList().get(0).getGrades().get(i).getName(), book.getGradeList().get(0).getGrades().get(i).getType(), book.getGradeList().get(0).getGrades().get(i).getDateOfAssignment(), studentAchievedScore, book.getGradeList().get(0).getGrades().get(i).getMaxScore()));
        }
        System.out.println(studentName + " added!");
    }

    // PRINT SINGLE ASSIGNMENT'S DETAILS -- OUTER CONTROLLER
    private static void printSingleAssignmentDetails(Scanner scanner, Book book) {
        System.out.print("Search by (1) Assignment's Name or (2) Date of Assignment? (1 or 2): ");  // GET USER CHOICE
        int choice = verifyNumericInput(scanner, 1, 2, false);

        switch (choice) {
            case 1:  // IF NAME
                System.out.print("Assignment's Name: ");  // GET NAME
                int index = book.searchForAssignmentName(scanner.next().toUpperCase());
                if (index == -1) {  // CHECK IF NAME IS IN BOOK
                    System.out.println("Assignment doesn't exist in book...");
                    return;
                } else
                    book.printSingleAssignmentDetails(index);  // PRESENT DATA
                break;
            case 2:  // IF DATE
                String assignmentDate = produceDate(scanner);  // GET DATA
                index = book.searchForAssignmentDate(assignmentDate);
                if (index == -1) {  // CHECK IF DATE IS IN BOOK
                    System.out.println("Assignment with date " + assignmentDate + " doesn't exist in book...");
                    return;
                } else
                    book.printSingleAssignmentDetails(index);  // PRESENT DATA
                break;
        }
    }

    // ADD ASSIGNMENT TO BOOK
    private static void addAssignment(Scanner scanner, Book book) {
        boolean nameClear = false;
        String assignmentName;
        do {
            System.out.print("Assignment's Name: ");  // GET ASSIGNMENT'S NAME
            assignmentName = scanner.next().toUpperCase();
            for (int i = 0; i < book.getGradeList().get(0).getGrades().size(); i++) {  // CHECK IF NAME ALREADY EXISTS
                if (book.getGradeList().get(0).getGrades().get(i).getName().equals(assignmentName)) {
                    System.out.println("Name already exists...");
                    nameClear = false;
                } else
                    nameClear = true;
            }
        } while (!nameClear);

        System.out.println();

        System.out.print("0 - Final Exam\n1 - Midterm Exam\n2 - Test\n3 - Quiz\n4 - Essay\n5 - Presentation\n6 - Other Project\n7 - Homework\n8 - Classwork\n9 - Participation\nAssignment Type (0-9): ");  // GET ASSIGNMENT TYPE
        int assignmentType = verifyNumericInput(scanner, 0, 9, true);

        System.out.println();

        String assignmentDate = produceDate(scanner);  // GET ASSIGNMENT DATE

        System.out.println();

        System.out.print("Maximum Possible Score: ");  // GET MAX SCORE
        int assignmentMaxScore = verifyNumericInput(scanner, 0, 1000, true);

        book.getGradeList().get(0).getGrades().add(new Assignment(assignmentName, assignmentType, assignmentDate, 0, assignmentMaxScore));  // CREATE NEW ASSIGNMENT FOR DUMMY STUDENT

        int assignmentIndex = book.getGradeList().get(0).getGrades().size() - 1;  // GET INDEX OF NEW ASSIGNMENT

        int assignmentAchievedScore;
        for (int i = 1; i < book.getClassList().size(); i++) {  // CREATE NEW ASSIGNMENT FOR OTHER STUDENTS
            System.out.print(book.getClassList().get(i).getName() + "'s Achieved Score: ");  // GET I'TH STUDENT'S ACHIEVED SCORE
            assignmentAchievedScore = verifyNumericInput(scanner, 0, book.getGradeList().get(0).getGrades().get(assignmentIndex).getMaxScore(), true);
            book.getGradeList().get(i).getGrades().add(new Assignment(assignmentName, assignmentType, assignmentDate, assignmentAchievedScore, assignmentMaxScore));
            System.out.println();
        }
        System.out.println(assignmentName + " added!");  // ANNOUNCE SUCCESS
    }

    // CHECK NUMERIC INPUT
    private static int verifyNumericInput(Scanner scanner, int lowEnd, int highEnd, boolean hasRange) {
        boolean isNumber = false;
        String input;
        int choice = -1;
        do {
            try {
                input = scanner.next();  // GET RAW INPUT
                choice = Integer.parseInt(input);  // TRY TO CONVERT TO INTEGER
                if (hasRange) {  // IF USER WANTS A RANGE OF NUMBERS POSSIBLE
                    if (choice < lowEnd || choice > highEnd) {
                        System.out.println("Number must be between " + lowEnd + " and " + highEnd + ".");
                        continue;
                    }
                } else {  // IF USER WANTS TWO NUMBERS POSSIBLE
                    if (choice != lowEnd && choice != highEnd) {
                        System.out.println("Number must be " + lowEnd + " or " + highEnd + ".");
                        continue;
                    }
                }
                isNumber = true;
            } catch (NumberFormatException e) {  // IF INTEGER CONVERSION WASN'T POSSIBLE
                System.out.println("Input not valid!");
            }
        } while (!isNumber);
        return choice;  // RETURN VALUE
    }

    // GET A DATE
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
