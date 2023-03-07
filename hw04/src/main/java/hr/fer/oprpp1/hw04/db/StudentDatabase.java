package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Scanner;

/**
 * A simple student record database.
 */
public class StudentDatabase {
    /**
     * List of student records.
     */
    private ArrayList<StudentRecord> records;

    /**
     * Map of student records, indexed by jmbag.
     */
    private HashMap<String, StudentRecord> jmbagMap;

    /**
     * The previous selection of records.
     */
    private ArrayList<StudentRecord> selection;

    /**
     * Remembers whether the previous selection was direct or not.
     */
    private boolean usedIndexed;

    /**
     * Creates a new student database from a list of student records.
     * 
     * @param records - list of student records
     */
    public StudentDatabase(List<String> lines) {
        records = new ArrayList<>();
        jmbagMap = new HashMap<>();
        selection = new ArrayList<>();

        for (String line : lines) {
            String[] rows = line.split("\t");
            if (rows.length != 4) {
                throw new IllegalArgumentException("Invalid number of arguments for line: " + line);
            }
            if (!checkGrade(rows[3])) {
                throw new IllegalArgumentException("Invalid grade for line: " + line);
            }
            if (jmbagMap.containsKey(rows[0])) {
                throw new IllegalArgumentException("Jmbag is not unique for line: " + line);
            }
            StudentRecord record = new StudentRecord(rows[0], rows[1], rows[2], Integer.parseInt(rows[3]));
            records.add(record);
            jmbagMap.put(rows[0], record);
        }
    }

    /**
     * Returns the student record with the given jmbag.
     * 
     * @param jmbag - jmbag of the student
     * @return student record with the given jmbag
     */
    public StudentRecord forJMBAG(String jmbag) {
        return jmbagMap.getOrDefault(jmbag, null);
    }

    /**
     * Returns a list of all records that satisfy the given filter.
     * 
     * @param filter - filter to be satisfied
     * @return - list of all records that satisfy the given filter
     */
    public List<StudentRecord> filter(IFilter filter) {
        return records.stream().filter(filter::accepts).collect(Collectors.toList());
    }

    /**
     * Checks if the provided grade is valid.
     * 
     * @param grade - the grade to check
     * @return - true if the grade is valid, false otherwise
     */
    private boolean checkGrade(Object grade) {
        try {
            int gradeInt = Integer.parseInt(grade.toString());
            return gradeInt >= 1 && gradeInt <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns a list of all records that satisfy the given query.
     * 
     * @param queryText - query to be satisfied
     * @return - list of all records that satisfy the given query
     */
    public void select(String queryText) {
        QueryParser parser = new QueryParser(queryText);
        selection.clear();
        if (parser.isDirectQuery()) {
            usedIndexed = true;
            StudentRecord record = forJMBAG(parser.getQueriedJMBAG());
            if (record != null) {
                selection.add(record);
            }
        } else {
            usedIndexed = false;
            selection = new ArrayList<>(filter(new QueryFilter(parser.getQuery())));
        }
    }

    /**
     * Returns the selection in a easily readable format.
     * 
     * @return the selection in a easily readable format
     */
    public String getResult() {
        if (selection.isEmpty()) {
            return "Records selected: 0\n";
        }
        int[] maxLen = { 0, 0, 0, 0 }; // jmbag, lastname, firstname, grade
        for (var record : selection) {
            maxLen[0] = Math.max(maxLen[0], record.getJmbag().length());
            maxLen[1] = Math.max(maxLen[1], record.getLastName().length());
            maxLen[2] = Math.max(maxLen[2], record.getFirstName().length());
            maxLen[3] = Math.max(maxLen[3], Integer.toString(record.getFinalGrade()).length());
        }
        StringBuilder sb = new StringBuilder();
        if (usedIndexed)
            sb.append("Using index for record retrieval.\n");
        appendVerticalBoundary(sb, maxLen);
        for (StudentRecord record : selection) {
            sb.append("|");
            String[] arrRecord = record.toArray();
            for (int i = 0; i < 4; i++) {
                sb.append(" ").append(arrRecord[i]);
                for (int w = 0; w < 1 + maxLen[i] - arrRecord[i].length(); w++) {
                    sb.append(" ");
                }
                sb.append("|");
            }
            sb.append("\n");
        }
        appendVerticalBoundary(sb, maxLen);
        sb.append("Records selected: ").append(selection.size()).append("\n");
        return sb.toString();
    }

    /**
     * Takes a StringBuilder and appends top and bottom boundaries to it based on
     * the number and length of columns.
     * 
     * @param sb - StringBuilder to append to
     * @param maxLen - array of maximum lengths of columns
     */
    private void appendVerticalBoundary(StringBuilder sb, int[] maxLen) {
        for (int i = 0; i < 4; i++) {
            sb.append("+");
            for (int j = 0; j < maxLen[i] + 2; j++) {
                sb.append("=");
            }
        }
        sb.append("+");
        sb.append("\n");
    }

    /**
     * The main method, this is to be called when working with the database.
     */
    public static void main(String[] args) throws IOException {
        StudentDatabase db = new StudentDatabase(Files.readAllLines(
                Paths.get("src/main/java/hr/fer/oprpp1/hw04/db/database.txt"),
                StandardCharsets.UTF_8));
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String query = sc.nextLine();
            if (query.equals("exit")) {
                break;
            }
            if (query.startsWith("query")) {
                db.select(query.substring(5));
                System.out.println(db.getResult());
            } else {
                System.out.println("Invalid command.");
            }
        }
        sc.close();
        System.out.println("Goodbye!");
    }

}
