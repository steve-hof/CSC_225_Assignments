package aggregate;

/* Aggregate.java
   CSC 225 - Summer 2018

   Some starter code for programming assignment 1, showing
   the command line argument parsing and the basics of opening
   and reading lines from the CSV file.

   B. Bird - 04/30/2018
*/


import java.io.*;
import java.util.*; //ArrayList;
import java.util.ArrayList;


public class Aggregate {

    // The first line of this method was taken from Stack Overflow https://stackoverflow.com/questions/14206768/how-to-check-if-a-string-is-numeric
    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static float convertNumeric(String element) {
        float float_element = Float.valueOf(element);
        return float_element;

    }


    public static void writeToConsole(String[][] array, String[] header, String fileName) {
        for (int i = 0; i < header.length; i++) {
            System.out.printf(header[i]);
            System.out.printf(",");
        }

        System.out.printf("\n");

        for (String[] row : array) {
            for (int i = 0; i < row.length; i++) {
                System.out.printf(row[i].toString());
                System.out.printf(",");
            }
            System.out.printf("\n");
        }
    }

    public static void writeToCsv(String[][] array, String[] header, String fileName) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            for (int i = 0; i < header.length; i++) {
                fileWriter.append(header[i]);
                fileWriter.append(",");
            }

            //Add a new line separator after the header
            fileWriter.append("\n");

            for (String[] row : array) {
                for (int i = 0; i < row.length; i++) {
                    fileWriter.append(row[i].toString());
                    fileWriter.append(",");
                }
                fileWriter.append("\n");

            }

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

    public static String[] removeEmpty(String[] oversized_array) {
        String[] removed_null = Arrays.stream(oversized_array)
                .filter(value ->
                        value != null && value.length() > 0
                )
                .toArray(size -> new String[size]);
        return removed_null;
    }

    public static void showUsage() {
        System.err.printf("Usage: java Aggregate <function> <aggregation column> <csv file> <group column 1> <group column 2> ...\n");
        System.err.printf("Where <function> is one of \"count\", \"count_distinct\", \"sum\", \"avg\"\n");
    }

    public static String[][] selectColumns(String[][] array, String[] col_names, String[] keep_cols) {

        int num_rows = array.length;
        int num_cols = keep_cols.length;
        String[][] pruned_array = new String[num_rows][num_cols];

        for (int i = 0; i < keep_cols.length; i++) {
            for (int j = 0; j < col_names.length; j++) {
                if (keep_cols[i].equals(col_names[j])) {
                    for (int k = 0; k < num_rows; k++) {
                        pruned_array[k][i] = array[k][j].toString();
                    }
                }
            }
        }

        return pruned_array;
    }

    public static String[] filterDuplicates(String [] duplicates_array) {
        boolean already_have = false;
        int count = duplicates_array.length;

        for (int i = 0; i < count; i++) {
            for (int j = i+1; j < count; j++) {
                if(duplicates_array[i].equals(duplicates_array[j])) {
                    duplicates_array[j] = duplicates_array[count-1];
                    count--;
                    j--;
                }
            }
        }
        String[] filtered_array = Arrays.copyOf(duplicates_array, count);

        return filtered_array;
    }

    public static ArrayList<String[][]> performSplit(String[] key_list, String[][] input_array) {
        ArrayList<String[][]> split_lists = new ArrayList<String[][]>();
        int num_cols = input_array[0].length;

        // go through each key and create 2d array of key with each value, then store in split_lists
        for (int i = 0; i < key_list.length; i++) {
            String curr_key = key_list[i];
            for (int j = 0; j < input_array.length; j++) {
                if (input_array[j][0].equals(curr_key)) {
                    String[][] curr_split_array = new String[input_array.length][num_cols];
                    for (int k = 0; k < )
                }
            }
        }
    }

    public static String[][] performAggregate(String[][] input_array) {
        // **** Split by group columns, output split_array(s) ****

        // create array of keys
        System.out.println("printing key array");
        String[] key_array = new String[input_array.length];
        for (int i = 0; i < input_array.length; i++) {
            key_array[i] = input_array[i][0];
        }

        String[] unique_keys = filterDuplicates(key_array);

        //
        ArrayList<String[][]> split_arrays = performSplit(unique_keys, input_array);


        // Apply function to each split_array, output applied_arrays




        // Combine applied_arrays into combined_array, return combined_array




        String[][] filler_array = new String[5][2];
        return filler_array;
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

        //Split the header_line string into an array of string values using a comma as the separator.
        String[] column_names = header_line.split(",");

        String row_line;
        ArrayList<String[]> csvData = new ArrayList<String[]>();

        try {
            while ((row_line = br.readLine()) != null) {
                //The readLine method returns either the next
                String[] row_array = row_line.split(",");
                csvData.add(row_array);
            }
        } catch (IOException e) {
            System.err.printf("Error reading file\n", csv_filename);
            return;
        }
//        if (row_line == null) {
//            System.err.printf("The file doesn't seem to have any data there fella", csv_filename);
//            return;
//        }
        // Turn ArrayList into 2D array
        String[][] full_data_array = new String[csvData.size()][column_names.length];
        csvData.toArray(full_data_array);

        /* create array of columns in proper order based on command line args
        * the column being aggregated is turned into floats and added at the end*/

        String[] cols_needed = new String[group_columns.length + 1];

        for (int i = 0; i < cols_needed.length - 1; i++) {
            cols_needed[i] = group_columns[i];
        }

        cols_needed[cols_needed.length - 1] = agg_column;

        String[][] trimmed_array = selectColumns(full_data_array, column_names, cols_needed);
        String[][] finished_array = performAggregate(trimmed_array);

        writeToConsole(finished_array, cols_needed, "csv_file.csv");

        int fill = 12;
    }

}
