package com.quiz;


import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Question {
    private String questionText;
    private String[] options;
    private char correctOption;

    public Question(String questionText, String optA, String optB, String optC, String optD, char correctOption) {
        this.questionText = questionText;
        this.options = new String[]{optA, optB, optC, optD};
        this.correctOption = Character.toUpperCase(correctOption);
    }

    public boolean ask(Scanner scanner) {
        System.out.println();
        System.out.println(questionText);
        System.out.println("A. " + options[0]);
        System.out.println("B. " + options[1]);
        System.out.println("C. " + options[2]);
        System.out.println("D. " + options[3]);

        char choice;
        while (true) {
            System.out.print("Enter your choice (A/B/C/D): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Please enter A, B, C or D.");
                continue;
            }
            choice = Character.toUpperCase(input.charAt(0));
            if (choice >= 'A' && choice <= 'D') break;
            else System.out.println("Invalid input. Please enter A, B, C or D.");
        }

        if (choice == correctOption) {
            System.out.println("Correct ");
            return true;
        } else {
            System.out.println("Wrong (Correct answer: " + correctOption + ")");
            return false;
        }
    }
}

public class QuizApp {
    private List<Question> questions;
    private int correctCount;
    private int wrongCount;

    public QuizApp(List<Question> questions) {
        this.questions = questions;
        this.correctCount = 0;
        this.wrongCount = 0;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Console Quiz Application ===");
        System.out.println("Number of questions: " + questions.size());
        System.out.println("Scoring: +1 for correct, -0.25 for wrong");
        System.out.println("---------------------------------");

        for (int i = 0; i < questions.size(); i++) {
            System.out.printf("Question %d/%d:\n", i + 1, questions.size());
            boolean correct = questions.get(i).ask(scanner);
            if (correct) correctCount++; else wrongCount++;
        }

        double score = correctCount * 1.0 + wrongCount * (-0.25);
        int totalQuestions = questions.size();
        double percentage = (score / totalQuestions) * 100.0;

        System.out.println();
        System.out.println("=== Quiz Summary ===");
        System.out.println("Total Questions : " + totalQuestions);
        System.out.println("Correct Answers : " + correctCount);
        System.out.println("Wrong Answers   : " + wrongCount);
        System.out.printf("Score           : %.2f\n", score);
        System.out.printf("Percentage      : %.2f%%\n", percentage);

        // Optional file save
        System.out.print("Do you want to save your score to a file? (Y/N): ");
        String saveChoice = scanner.nextLine().trim();
        if (!saveChoice.isEmpty() && Character.toUpperCase(saveChoice.charAt(0)) == 'Y') {
            System.out.print("Enter your name (for the file): ");
            String name = scanner.nextLine().trim();

            if (name.isEmpty()) name = "Anonymous";
            saveScoreToFile(name, score, percentage, totalQuestions);
        }

        System.out.println("Thank you for taking the quiz!");
        scanner.close();
    }

    private void saveScoreToFile(String name, double score, double percentage, int totalQuestions) {
        String filename = "scores.txt";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(fmt);

        String line = String.format("%s | Name: %s | Score: %.2f | Percentage: %.2f%% | Out Of: %d%n",
                timestamp, name, score, percentage, totalQuestions);

        try (FileWriter fw = new FileWriter(filename, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(line);
            bw.flush(); // ensures it’s actually written
            System.out.println("Score saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Failed to save score: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        List<Question> qs = new ArrayList<>();

        qs.add(new Question("Which keyword is used to create an object in Java?", "new", "create", "construct", "init", 'A'));
        qs.add(new Question("What does HTTP stand for?", "HyperText Transfer Protocol", "HighText Transfer Program", "HyperText Transmission Protocol", "Hyperlink Transfer Protocol", 'A'));
        qs.add(new Question("Which data structure uses FIFO order?", "Stack", "Queue", "Tree", "Graph", 'B'));
        qs.add(new Question("Which primitive type holds true/false values in Java?", "int", "double", "boolean", "char", 'C'));
        qs.add(new Question("Which HTML tag is used to create a hyperlink?", "<a>", "<link>", "<href>", "<hyper>", 'A'));
        qs.add(new Question("Which sorting algorithm has average time complexity O(n log n)?", "Bubble Sort", "Selection Sort", "Quick Sort", "Insertion Sort", 'C'));
        qs.add(new Question("In OOP, what does 'encapsulation' mean?", "Hiding implementation details and exposing interface", "Creating many classes", "Using only static methods", "Writing code without comments", 'A'));
        qs.add(new Question("Which SQL command is used to remove a table and its data?", "DELETE TABLE table_name;", "DROP TABLE table_name;", "REMOVE table_name;", "TRUNCATE DATABASE table_name;", 'B'));
        qs.add(new Question("Which method starts the execution of a Java program?", "start()", "main()", "run()", "execute()", 'B'));
        qs.add(new Question("Which symbol is used to denote a single-line comment in Java?", "//", "/*", "#", "<!-- -->", 'A'));

        QuizApp app = new QuizApp(qs);
        app.run();
    }
}
