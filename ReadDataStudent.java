
import java.io.File;
import java.util.Scanner;

public class ReadDataStudent {
// attempt at allowing the array initialization use actual filesize and information programatically, this can be built upon to reveal more information if needed?

    public int fileInfo(String filename, int selection) {
        try {
            Scanner scanner = new Scanner(new File(filename));

            if (selection == 1) {
                int rowcount = 0;
                while (scanner.hasNextLine()) {
                    rowcount++;
                    scanner.nextLine();
                }
                return rowcount;
            } else if (selection == 2) {
                int colcount = 0;
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] lineArr = line.split(",");
                    colcount = lineArr.length;
                }
                return colcount;
            } else {
                return -1;
            }

        } catch (Exception e) {
            System.out.println("Error occured: " + e);
        }
        return -1;

    }
    //I hard-coded the number of rows and columns so 
    //I could use a 2D array

    private double[][] data = new double[fileInfo("cps.csv", 1)][fileInfo("cps.csv", 2)];

    //This should read in the csv file and store the data in a 2D array,
    //data -- don't forget to skip the header line and parse everything
    //as doubles  
    public void read() {
        try {
            Scanner scanner = new Scanner(new File("cps.csv"));
            int row = 0;
            scanner.nextLine(); // Skip the header line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] lineArr = line.split(",");
                for (int i = 0; i < lineArr.length; i++) {
                    data[row][i] = Double.parseDouble(lineArr[i]);
                }
                row++;

            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //this should return the column of data based
    //on the column number passed in -- the column number
    //is 0 indexed, so the first column is 0, the second
    //is 1, etc.
    //this should return a double array of the column
    //of data
    public double[] getColumn(int col) {
        double[] column = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            column[i] = data[i][col];
        }
        return column;
    }

    //this returns the standard deviation of the column
    //of data passed in
    //the standard deviation is the square root of the variance
    //the variance is the sum of the squares of the differences
    //between each value and the mean, 
    //divided by the number of values - 1(sample variance)
    //Use Math.pow to square the difference
    //and Math.sqrt to take the square root
    public double stdDeviation(double[] arr) {
        double sum = 0;
        double mean = mean(arr);
        double squaresum = 0;
        for (int i = 0; i < arr.length; i++) {
            squaresum += (Math.pow(arr[i] - mean, 2));
        }
        return (Math.sqrt(squaresum / arr.length)- 1);
    }

    //this returns the mean of the column of data passed in
    //the mean is the sum of the values divided by the number of values
    public double mean(double[] arr) {
        double sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return (sum / arr.length);
    }

    //this returns the values of a column in standard units
    //the standard units are the value minus the mean divided by the standard deviation
    //this should return a double array of the standard units
    public double[] standardUnits(double[] arr) {
        double[] stdArr = new double[arr.length];
        double mean = mean(arr);
        double stdrdev = stdDeviation(arr);

        for (int i = 0; i < arr.length; i++) {
            stdArr[i] = ((arr[i] - mean) / stdrdev);
        }
        return stdArr;

    }

    //this returns the correlation between the two columns of data passed in
    //the correlation is the sum of the products of the standard units
    //of the two columns divided by the number of values - 1
    //this should return a double
    //the correlation is a measure of the strength of the linear relationship
    //between the two columns of data
    //the correlation is between -1 and 1
    public double correlation(double[] x, double[] y) {
        double temphold = 0.0;
        double[] t = standardUnits(x);
        double[] r = standardUnits(y);
        if (t.length == r.length) {
            for (int i = 0; i < x.length; i++) {
                temphold += (t[i]* r[i]);
            }
            return ((temphold/t.length)-1);
        } else {
            return -1;
        }
    }

    public void runRegression() {
        double[] x = getColumn(7);
        double[] y = getColumn(9);
        double[] xStd = standardUnits(x);
        double[] yStd = standardUnits(y);
        double correlation = correlation(xStd, yStd);
        double slope = correlation * stdDeviation(y) / stdDeviation(x);
        double intercept = mean(y) - slope * mean(x);
        System.out.println("Correlation: " + correlation);
        System.out.println("Slope: " + slope);
        System.out.println("Intercept: " + intercept);
        Scatter s = new Scatter();
        s.displayScatterPlot(x, y);
    }

    //this prints the array passed in - you may want this for debugging
    public void print(double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ReadDataStudent rd = new ReadDataStudent();
        rd.read();
        rd.runRegression();
    }

}
