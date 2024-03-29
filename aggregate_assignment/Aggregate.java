
/* Aggregate.java
   CSC 225 - Summer 2018

   Some starter code for programming assignment 1, showing
   the command line argument parsing and the basics of opening
   and reading lines from the CSV file was done by B. Bird


   The rest was completed by Steve Hof V00320492
*/


import java.io.*;
import java.lang.reflect.Array;
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


    public static void writeToConsole(ArrayList<String[]> csv_list, String[] header) {
        for (int i = 0; i < header.length; i++) {
            System.out.print(header[i]);
            System.out.print(",");
        }

        System.out.print("\n");

        for (String[] sub_list : csv_list) {
            for (int i = 0; i < sub_list.length; i++) {
                System.out.print(sub_list[i]);
                System.out.print(",");
            }
            System.out.print("\n");
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
                        pruned_array[k][i] = array[k][j];
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

    public static float performSum(float[] agg_array) {
        float agg_result = 0;
        for (int i = 0; i < agg_array.length; i++) {
            agg_result = agg_result + agg_array[i];
        }
        return agg_result;
    }

    public static float performCount(float[] agg_array) {
        float agg_result = 0;
        for (int i = 0; i < agg_array.length; i++) {
            agg_result = agg_result + 1;
        }
        return agg_result;
    }

    public static float performAvg(float[] agg_array) {
        float agg_result = 0;
        float sum = performSum(agg_array);
        float count = performCount(agg_array);
        agg_result = sum / count;
        return agg_result;
    }

    private static float performDistinct(ArrayList<String> total_list) {
        Collections.sort(total_list);
        float count = 1;
        for (int k = 0; k < total_list.size() - 1; k++) {
            int j = k + 1;
            if(!total_list.get(k).equals(total_list.get(j))) {
                count++;
            }
        }
        return count;
    }
    private static String applyFunc(ArrayList<String> data_column, String func) {
        int num_rows = data_column.size();
        float agg_result = 0;
        float[] agg_array = new float[num_rows];
        if (func.equals("count_distinct")) {
            agg_result = performDistinct(data_column);

            return Float.toString(agg_result);
        }

        for (int i = 0; i < num_rows; i++) {
            agg_array[i] = convertNumeric(data_column.get(i));
        }
        if (func.equals("sum")) {
            agg_result = performSum(agg_array);

        } else if (func.equals("count")) {
            agg_result = performCount(agg_array);

        } else if (func.equals("avg")) {
            agg_result = performAvg(agg_array);
        }

        String agg_result_string = Float.toString(agg_result);
        return agg_result_string;
    }



    private static ArrayList<String[]> sortToList(String[][] data_array) {

        Arrays.sort(data_array, (entry1, entry2) -> {
            final String key_1 = entry1[0];
            final String key_2 = entry2[0];
            return key_1.compareTo(key_2);
        });


        ArrayList<String[]> sortedList = new ArrayList<>();
        for (String[] row : data_array) {
            Collections.addAll(sortedList, row);
        }

        return sortedList;
    }


    private static ArrayList<ArrayList<String[]>>performSplit(int sort_column, ArrayList<ArrayList<String[]>> data, String[] keys) {
        ArrayList<ArrayList<String[]>> smaller_lists = new ArrayList<>();

        for (ArrayList<String[]> sublist : data) {
            for (String key : keys) {
                ArrayList<String[]> templist = new ArrayList<>();
                for (int i = 0; i < sublist.size(); i++) {
                    if (sublist.get(i)[sort_column].equals(key)) {
                        templist.add(sublist.get(i));
                    }
                }
                smaller_lists.add(templist);
            }
        }


        return smaller_lists;
    }

    private static ArrayList<String[]> performAggregate(String[][] input_array, String agg_func, String[] cols_needed) {
        // **** Split by group columns, output split_array(s) ****

        // Turn input_array into a sorted list
        ArrayList<String[]> sorted_list = sortToList(input_array);

        // create list of arrays of keys
        ArrayList<String[]> key_list = new ArrayList<>();

        for (int i = 0; i < input_array[0].length - 1; i++) {
            String[] sub_key_array = new String[input_array.length];
            for (int j = 0; j < input_array.length; j++) {
                sub_key_array[j] = input_array[j][i];
            }
            String[] temp = filterDuplicates(sub_key_array);
            key_list.add(temp);

        }

//        String[] cols_needed_minus_data = new String[cols_needed.length - 1];
//        for (int i = 0; i < cols_needed.length - 1; i++) {
//            cols_needed_minus_data[i] = cols_needed[i];
//        }

        ArrayList<ArrayList<String[]>> big_list = new ArrayList<>();
        big_list.add(sorted_list);

        for (int i = 0; i < key_list.size(); i++) {
            big_list = performSplit(i, big_list, key_list.get(i));
        }

        ArrayList<String[]> final_list = new ArrayList<>();
        int split_list_num = 0;
        for (ArrayList<String[]> split_list : big_list) {
            if (split_list.size() > 0) {

                int data_col_num = split_list.get(0).length - 1;
                String[] final_agg = new String[split_list.get(0).length];
                for (int i = 0; i < data_col_num; i++) {
                    final_agg[i] = split_list.get(0)[i];
                }

                ArrayList<String> data_col = new ArrayList<>();

                for (int j = 0; j < split_list.size(); j++) {
                    data_col.add(split_list.get(j)[data_col_num]);
                }

                final_agg[data_col_num] = applyFunc(data_col, agg_func);
                final_list.add(final_agg);
            }

            split_list_num++;

        }

        return final_list;
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
        String final_col = agg_function + "(" + agg_column + ")";
        cols_needed[cols_needed.length - 1] = agg_column;

        String[][] trimmed_array = selectColumns(full_data_array, column_names, cols_needed);
        ArrayList<String[]> finished_list = performAggregate(trimmed_array, agg_function, cols_needed);
        cols_needed[cols_needed.length - 1] = final_col;
        writeToConsole(finished_list, cols_needed);

        int fill = 12;
    }

}


