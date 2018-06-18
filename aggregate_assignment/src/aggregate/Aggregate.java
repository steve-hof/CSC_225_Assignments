package aggregate;

/* Aggregate.java
   CSC 225 - Summer 2018

   Some starter code for programming assignment 1, showing
   the command line argument parsing and the basics of opening
   and reading lines from the CSV file was written by B. Bird.

   Rest of it was completed by Steve Hof V00320492
*/


import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.ArrayList;


public class Aggregate {

    // isNumeric was taken from:
    // https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    /* Running Time: O(1) */
    private static float convertNumeric(String element) {
        float float_element = Float.valueOf(element);

        return float_element;
    }


    /*
        ************************ writeToConsole ************************
        * Printing the header does not depend on number of rows so O(1)
        *
        * The the outer nested for loop could be n in the worst case,
        * however, the inner for loop depends on the number of columns
        * to aggregate, not on the number of samples (rows)
        *
        * ******** Running Time: O(n)
     */

    private static void writeToConsole(ArrayList<String[]> csv_list, String[] header) {
        // Print header
        System.out.print(header[0]);
        for (int i = 1; i < header.length; i++) {
            System.out.print(",");
            System.out.print(header[i]);
        }

        System.out.print("\n");

        // print data
        for (int i = 0; i < csv_list.size(); i++) {
            System.out.print(csv_list.get(i)[0]);
            for (int j = 1; j < csv_list.get(i).length; j++) {
                System.out.print(",");
                System.out.print(csv_list.get(i)[j]);
            }

            System.out.print("\n");
        }
    }

    // Running Time: O(1)
    private static void showUsage() {
        System.err.printf("Usage: java Aggregate <function> <aggregation column> <csv file> <group column 1> <group column 2> ...\n");
        System.err.printf("Where <function> is one of \"count\", \"count_distinct\", \"sum\", \"avg\"\n");
    }


    /*
        ************************ selectColumns ************************
        * Although there is a 3 level nested loop, only the last for loop is
        * a function of the number of samples (rows), therefore
        *
        * ***** Running Time: O(n)
        *
    */

    private static String[][] selectColumns(String[][] array, String[] col_names, String[] keep_cols) {

        int num_rows = array.length;
        int num_cols = keep_cols.length;
        String[][] pruned_array = new String[num_rows][num_cols];

        for (int i = 0; i < keep_cols.length; i++) {
            for (int j = 0; j < col_names.length; j++) {
                if (keep_cols[i].equals(col_names[j])) {
                    for (int k = 0; k < num_rows; k++) {
                        if (j >= array[k].length) {
                            continue;
                        } else {
                            pruned_array[k][i] = array[k][j];
                        }

                    }
                }
            }
        }

        return pruned_array;
    }

    /*
     ************************ filter_duplicates ************************
     * Arrays.sort (for objects) is guaranteed O(nlogn)
     *
     * My filtering algorithm consists of one for loop that is O(n)
     *
     * Arrays.copyOf runs in O(n)
     *
     * ******* Running Time: O(nlogn)

     */

    private static String[] filterDuplicates(String[] duplicates_array) {

        // sort O(nlogn)
        Arrays.sort(duplicates_array);

        // filtering algorithm is O(n)
        int count = 1;
        String[] filtered_array = new String[duplicates_array.length];
        filtered_array[0] = duplicates_array[0];
        for (int i = 0; i < duplicates_array.length - 1; i++) {
            int j = i + 1;
            if (!duplicates_array[i].equals(duplicates_array[j])) {
                filtered_array[count] = duplicates_array[j];
                count++;
            }
        }

        filtered_array = Arrays.copyOf(filtered_array, count);

        return filtered_array;
    }


    /*
         ************************ perform____ (func) ************************
         * All of performSum, performCount, performAvg are a single for loop
         * that depends on n.
         *
         * *********** Running Time: O(n) (for all three methods)
     */

    private static float performSum(float[] agg_array) {
        float agg_result = 0;
        for (int i = 0; i < agg_array.length; i++) {
            agg_result = agg_result + agg_array[i];
        }
        return agg_result;
    }


    private static float performCount(float[] agg_array) {
        float agg_result = agg_array.length;

        return agg_result;
    }


    private static float performAvg(float[] agg_array) {
        float agg_result;
        float sum = performSum(agg_array);
        float count = agg_array.length;
        agg_result = sum / count;
        return agg_result;
    }


    /*
         ************************ performDistinct ************************
         * documentation: https://docs.oracle.com/javase/7/docs/api/java/util/Collections.html
         *
         * According to documentation above, Collections.sort uses a modified merge sort and
         * its running time is O(nlogn)
         *
         * The for loop runs in O(n)
         *
         * ********* Running Time: O(nlogn)
     */

    private static float performDistinct(ArrayList<String> total_list) {
        Collections.sort(total_list);
        float count = 1;
        for (int k = 0; k < total_list.size() - 1; k++) {
            int j = k + 1;
            if (!total_list.get(k).equals(total_list.get(j))) {
                count++;
            }
        }
        return count;
    }


    /*  ************************ applyFunc ************************
        * Takes in the column that the aggregate function is being applied to,
        * converts the elements to floats if necessary and passes it on to the
        * appropriate function.

        ******nRunning Time: O(n)

    */
    private static String applyFunc(ArrayList<String> data_column, String func) {
        int num_rows = data_column.size();
        float agg_result = 0;
        float[] agg_array = new float[num_rows];
        if (func.equals("count_distinct")) {
            agg_result = performDistinct(data_column);
            return Float.toString(agg_result);

        } else if (func.equals("count")) {
            agg_result = performCount(agg_array);
            return Float.toString((agg_result));
        }

        for (int i = 0; i < num_rows; i++) {
            if (isNumeric(data_column.get(i))) {
                agg_array[i] = convertNumeric(data_column.get(i));
            } else {
                throw new NumberFormatException("ERROR: The aggregation column contains non numeric values");
            }

        }
        if (func.equals("sum")) {
            agg_result = performSum(agg_array);

        } else if (func.equals("avg")) {
            agg_result = performAvg(agg_array);
        }

        return Float.toString(agg_result);
    }


    /*
        ************************ performSplit ************************
        * STILL NEED TO FIGURE OUT
        *
        *
        * ******* Running Time: ?
     */
    private static ArrayList<ArrayList<String[]>> performSplit(int sort_column, ArrayList<ArrayList<String[]>> data, String[] keys) {
        ArrayList<ArrayList<String[]>> smaller_lists = new ArrayList<>();

        for (ArrayList<String[]> sublist : data) {
            for (String key : keys) {
                ArrayList<String[]> templist = new ArrayList<>();
                for (String[] aSublist : sublist) {
                    if (aSublist[sort_column].equals(key)) {
                        templist.add(aSublist);
                    }
                }
                smaller_lists.add(templist);
            }
        }

        return smaller_lists;
    }

    /*
         ************************ performAggregate ************************
         * STILL NEED TO FIGURE OUT
         *
         *
         * ******* Running Time: ?
     */

    private static ArrayList<String[]> performAggregate(String[][] input_array, String agg_func, String[] cols_needed) {
        // **** Split by group columns, output split_array(s) ****

        ArrayList<String[]> list = new ArrayList<>();
        for (String[] row : input_array) {
            Collections.addAll(list, row);
        }

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

        ArrayList<ArrayList<String[]>> big_list = new ArrayList<>();
        big_list.add(list);

        for (int i = 0; i < key_list.size(); i++) {
            big_list = performSplit(i, big_list, key_list.get(i));
        }

        ArrayList<String[]> final_list = new ArrayList<>();
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
                /*
                    got the remove all function below from:
                    https://stackoverflow.com/questions/4819635/how-to-remove-all-null-elements-from-a-arraylist-or-string-array
                 */
                data_col.removeAll(Collections.singleton(null));
                final_agg[data_col_num] = applyFunc(data_col, agg_func);
                final_list.add(final_agg);
            }
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
            System.err.printf("That is not a valid aggregate function.");
            showUsage();
            return;
        }
        if (Arrays.asList(group_columns).contains(agg_column)) {
            System.err.printf("the aggregation column cannot be the same as any other requested column\n");
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
        ArrayList<String[]> csvData = new ArrayList<>();

        try {
            while ((row_line = br.readLine()) != null) {
                //The readLine method returns either the next
                String[] row_array = row_line.split(",");
                csvData.add(row_array);
            }
        } catch (IOException e) {
            System.err.printf("Error reading file %s\n", csv_filename);
            return;
        }

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

        ArrayList<String> requested_columns = new ArrayList<>(Arrays.asList(group_columns));
        ArrayList<String> actual_columns = new ArrayList<>(Arrays.asList(column_names));

        requested_columns.add(agg_column);

        for (String col : requested_columns) {
            if (!actual_columns.contains(col)) {
                System.err.printf("You have requested an invalid column\n");
                return;
            }
        }

        String[][] trimmed_array = selectColumns(full_data_array, column_names, cols_needed);

        ArrayList<String[]> finished_list = performAggregate(trimmed_array, agg_function, cols_needed);

        cols_needed[cols_needed.length - 1] = final_col;

        writeToConsole(finished_list, cols_needed);
        }

}


