package aggregate;

/* Aggregate.java
   CSC 225 - Summer 2018

   Some starter code for programming assignment 1, showing
   the command line argument parsing and the basics of opening
   and reading lines from the CSV file.

   B. Bird - 04/30/2018
*/


import java.io.*;
import java.util.ArrayList;

public class Aggregate {

    // The first line of this method was taken from Stack Overflow https://stackoverflow.com/questions/14206768/how-to-check-if-a-string-is-numeric
    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }
    // based on code found at https://examples.javacodegeeks.com/core-java/writeread-csv-files-in-java-example/
    public static void writeToCsv(ArrayList<String[]> csv_Data, String header, String fileName){
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(header);

//            //Add a new line separator after the header
            fileWriter.append("\n");

            int size = csv_Data.size();
            for (String[] cell : csv_Data) {
                for (int i = 0; i < cell.length; i++){
                    System.out.println("this should be data:" + cell[i].toString() + "and I hope it is");
                    fileWriter.append(cell[i].toString());
                    fileWriter.append(",");
                }
                fileWriter.append("\n");
            }

//            for(int i=0;i<csv_Data.size();i++){
//
//                String[] row = new String[4];
//                myString=outerArr.get(i);
//                for(int j=0;j<myString.length;j++){
//                    System.out.print(myString[j]);
//                }
//                System.out.print("\n");
//
//            }
            //Write a new student object list to the CSV file
//            for (Student student : students) {
//                fileWriter.append(String.valueOf(student.getId()));
//                fileWriter.append(COMMA_DELIMITER);
//                fileWriter.append(student.getFirstName());
//                fileWriter.append(COMMA_DELIMITER);
//                fileWriter.append(student.getLastName());
//                fileWriter.append(COMMA_DELIMITER);
//                fileWriter.append(student.getGender());
//                fileWriter.append(COMMA_DELIMITER);
//                fileWriter.append(String.valueOf(student.getAge()));
//                fileWriter.append(NEW_LINE_SEPARATOR);
//        }

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }

    public static void showUsage() {
        System.err.printf("Usage: java Aggregate <function> <aggregation column> <csv file> <group column 1> <group column 2> ...\n");
        System.err.printf("Where <function> is one of \"count\", \"count_distinct\", \"sum\", \"avg\"\n");
    }


    public static void main(String[] args) {

        //At least four arguments are needed
        if (args.length < 4) {
            showUsage();
            return;
        }
        String agg_function = args[0];
        String agg_column = args[1];
        String csv_filename = args[2];
        String[] group_columns = new String[args.length - 3];
        for (int i = 3; i < args.length; i++)
            group_columns[i - 3] = args[i];

        if (!agg_function.equals("count") && !agg_function.equals("count_distinct")
                && !agg_function.equals("sum") && !agg_function.equals("avg")) {
            System.out.println("Looking for data file");
            showUsage();
            return;
        }

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(csv_filename));
        } catch (IOException e) {
            System.err.printf("Error: Unable to open file %s\n", csv_filename);
            return;
        }

        // get header info
        String header_line;

        try {
            header_line = br.readLine(); //The readLine method returns either the next line of the file or null (if the end of the file has been reached)
        } catch (IOException e) {
            System.err.printf("Error reading file\n", csv_filename);
            return;
        }
        if (header_line == null) {
            System.err.printf("Error: CSV file %s has no header row\n", csv_filename);
            return;
        }

        // get data info
        String row_line;
        ArrayList<String[]> csvData = new ArrayList<String[]>();

        try {
            while((row_line = br.readLine())!=null) {
                //The readLine method returns either the next
                System.out.println(row_line);
                String[] row_array = row_line.split(",");
                csvData.add(row_array);
                int fill = 12;
//                return;
            }
        } catch (IOException e) {
            System.err.printf("Error reading file\n", csv_filename);
            return;
        }
//        if (row_line == null) {
//            System.err.printf("The file doesn't seem to have any data there fella", csv_filename);
//            return;
//        }

        //Split the header_line string into an array of string values using a comma
        //as the separator.
        String[] column_names = header_line.split(",");

        //As a diagnostic, print out all of the argument data and the column names from the CSV file
        //(for testing only: delete this from your final version)

        System.out.println("Aggregation function: " + agg_function);
        System.out.println("Aggregation column: " + agg_column);


        for (String s : group_columns)
            System.out.println("Grouping column: " + s);
        System.out.println();

        for (String s : column_names)
            System.out.println("CSV column name: " + s);


        //... Your code here ...
        writeToCsv(csvData, header_line, "csv_file.csv");
        int fill = 12;
    }

}
