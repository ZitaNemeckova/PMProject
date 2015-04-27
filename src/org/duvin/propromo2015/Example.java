/**
 *
 * @author Johannes Verwijnen <duvin@duvin.org>
 */
 package org.duvin.propromo2015;

import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Example {

    private static String refineryId = "";
    private static String currentContext;
    private static String currentRequest;
    private static String currentInput;
    private static String currentWork;
    private static ExampleWorker worker;
    private static int[][] context;
	private static int round = 0;
	private static double minimalValue = 0.00000000001;
	private static double[] distribution ;
	private static double[][] transaction;
	private static double [][] prior;
	private static double [][] temp;
    private static DecimalFormat df = new DecimalFormat();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        

        Scanner s = new Scanner(System.in);
        //let repository know we're alive
        System.out.println("HELLO\t1.0");

        while (true) {
            String command = s.nextLine();
            String[] parts = command.split("\\t");
            if (command.startsWith("SETUP\t")) {
                refineryId = parts[1];
                System.out.println("ALIVE\t" + refineryId + "\tinfo=Test program\tversion=0.666");
            } else if (command.startsWith("PERFORM\t") && !refineryId.isEmpty()) {
                currentRequest = parts[1];
                currentContext = parts[2];
                currentWork = parts[3];
                currentInput = parts[4];
                worker = new ExampleWorker();
                worker.start();
            } else if (command.startsWith("ABORT\t") && !refineryId.isEmpty()) {
                worker.cancel();
            } else if (command.startsWith("PING\t")) {
                System.out.println("PONG\t" + parts[1]);
            } else {
                System.err.println("Uhhuh!\nReceived: " + command);
                System.exit(0);
            }
        }
    }

    public static String arrayToCSV(double[] a) {
        df.setMaximumFractionDigits(340);
        df.setMinimumFractionDigits(1);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        StringBuilder sb = new StringBuilder();
        for (double d : a) {
            sb.append(df.format(d)).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * @param args the command line arguments
     */
     private static double[] normalizeDistribution(double[] dis){
         double norm = 0;
         for(double probability:dis)
             norm = norm + probability;
         for(int i = 0; i < dis.length; i++)
             dis[i] = dis[i]/norm;
         return dis;
     }
     private static double[] getDistribution(int position) throws IOException{
         double[] dis = new double[100];
         if(round == 0){
             dis = distribution;
			 round++;
         }
         else {
            for(int i = 0; i < 100; i++){
                temp[position][i] = (prior[position][i]* transaction[context[round -2][i]][i])/prior[position][context[round -2][i]];
            }
            //UPDATE
            prior = temp;
			round++;
         }
         return normalizeDistribution(dis);
     }

   public static void initialize() throws FileNotFoundException, IOException{
       prior = new double[1000][100];
        context = new int[303][1000];
        transaction = new double[100][100];
        distribution = new double[100];
        temp = new double [1000][100];
        distribution[0] = 0.942314050142121 ;distribution[1] = 4.79975886011487E-08 ;distribution[2] = 4.79975886011487E-08 ;distribution[3] = 4.79975886011487E-08 ;distribution[4] = 4.79975886011487E-08 ;distribution[5] = 4.79975886011487E-08 ;distribution[6] = 4.79975886011487E-08 ;distribution[7] = 4.79975886011487E-08 ;distribution[8] = 4.79975886011487E-08 ;distribution[9] = 4.79975886011487E-08 ;distribution[10] = 4.79975886011487E-08 ;distribution[11] = 4.79975886011487E-08 ;distribution[12] = 4.79975886011487E-08 ;distribution[13] = 4.79975886011487E-08 ;distribution[14] = 4.79975886011487E-08 ;distribution[15] = 4.79975886011487E-08 ;distribution[16] = 4.79975886011487E-08 ;distribution[17] = 4.79975886011487E-08 ;distribution[18] = 4.79975886011487E-08 ;distribution[19] = 4.79975886011487E-08 ;distribution[20] = 4.79975886011487E-08 ;distribution[21] = 4.79975886011487E-08 ;distribution[22] = 4.79975886011487E-08 ;distribution[23] = 4.79975886011487E-08 ;distribution[24] = 4.79975886011487E-08 ;distribution[25] = 4.79975886011487E-08 ;distribution[26] = 4.79975886011487E-08 ;distribution[27] = 1.43992765803446E-07 ;distribution[28] = 9.59951772022974E-08 ;distribution[29] = 4.79975886011487E-08 ;distribution[30] = 1.43992765803446E-07 ;distribution[31] = 9.59951772022974E-08 ;distribution[32] = 4.79975886011487E-07 ;distribution[33] = 6.23968651814933E-07 ;distribution[34] = 5.6157178663344E-06 ;distribution[35] = 8.39957800520102E-06 ;distribution[36] = 0.000133625286665598 ;distribution[37] = 0.000492167273516179 ;distribution[38] = 0.000468168479215604 ;distribution[39] = 0.000638991897047092 ;distribution[40] = 0.00106631442836312 ;distribution[41] = 0.000902018682581387 ;distribution[42] = 0.0013205576551834 ;distribution[43] = 0.00131686184086112 ;distribution[44] = 0.000942912628069566 ;distribution[45] = 0.00114013471963169 ;distribution[46] = 0.00100377357041582 ;distribution[47] = 0.000771321248820459 ;distribution[48] = 0.00104754737122007 ;distribution[49] = 0.000853541118094227 ;distribution[50] = 0.000586002559231424 ;distribution[51] = 0.000713532152144676 ;distribution[52] = 0.000525333607239572 ;distribution[53] = 0.000364973663723135 ;distribution[54] = 0.000457705004900554 ;distribution[55] = 0.00044628157881348 ;distribution[56] = 0.000303632745490867 ;distribution[57] = 0.000305696641800716 ;distribution[58] = 0.000265378667375751 ;distribution[59] = 0.000415851107640352 ;distribution[60] = 0.000803143650063021 ;distribution[61] = 0.000642495721014976 ;distribution[62] = 0.00094977628323953 ;distribution[63] = 0.00195114997422529 ;distribution[64] = 0.00133452495346634 ;distribution[65] = 0.00211218188398215 ;distribution[66] = 0.00376325093427306 ;distribution[67] = 0.00169777070399983 ;distribution[68] = 0.00225756657985503 ;distribution[69] = 0.00426256984849081 ;distribution[70] = 0.00199357984254871 ;distribution[71] = 0.0024528207702845 ;distribution[72] = 0.00458919343892163 ;distribution[73] = 0.00171629777319987 ;distribution[74] = 0.00187262591927382 ;distribution[75] = 0.00362568984534217 ;distribution[76] = 0.00136884322931616 ;distribution[77] = 0.00119969972708571 ;distribution[78] = 0.00147026213403039 ;distribution[79] = 0.00035892596755939 ;distribution[80] = 0.000230772405994323 ;distribution[81] = 0.00030713656945875 ;distribution[82] = 9.83470590437536E-05 ;distribution[83] = 7.07484455980932E-05 ;distribution[84] = 5.20293860436452E-05 ;distribution[85] = 3.93580226529419E-06 ;distribution[86] = 6.71966240416081E-07 ;distribution[87] = 1.43992765803446E-07 ;distribution[88] = 4.79975886011487E-08 ;distribution[89] = 4.79975886011487E-08 ;distribution[90] = 4.79975886011487E-08 ;distribution[91] = 4.79975886011487E-08 ;distribution[92] = 4.79975886011487E-08 ;distribution[93] = 4.79975886011487E-08 ;distribution[94] = 4.79975886011487E-08 ;distribution[95] = 4.79975886011487E-08 ;distribution[96] = 4.79975886011487E-08 ;distribution[97] = 4.79975886011487E-08 ;distribution[98] = 4.79975886011487E-08 ;distribution[99] = 4.79975886011487E-08 ;
		for(int i = 0; i < 1000; i++){
            prior[i] = distribution;
        }
		final String dir = System.getProperty("user.dir");
		System.out.println("current dir = " + dir);
		int currentLine = 0;
		BufferedReader br = new BufferedReader(new FileReader(dir + "\\case.txt"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				String[] parts = line.split(";");
				int row = 0;
				for(String number: parts){
					double value = Double.parseDouble(number);
					transaction[currentLine][row] = value;
					row++;
				}
				line = br.readLine();
				currentLine ++;
			}
		} finally {
			br.close();
		}
    //for(int i = 0; i < 100; i++)
      //  for(int j = 0; j < 100; j++)
        //   System.out.println(transaction[i][j]);
   }

    public static class ExampleWorker extends Thread {

        @Override
        public void run() {
            double[][] myGuess = new double[1000][100];
            File inputDir = new File(currentInput);
            //just take first file
            File inputFile = inputDir.listFiles()[0];
            try {
                Scanner s = new Scanner(inputFile);
                File outputDir = new File(currentWork + "/output");
                if (!outputDir.exists()) {
                    outputDir.mkdir();
                }
                File outputFile = new File(outputDir, "output.csv");
                PrintWriter pw = new PrintWriter(outputFile);
                if (round++ == 0){
                    //don't have context yet
                    context = new int[303][1000];
					initialize();
				}
                for (int i = 0; i < 1000; i++) {
					double[] dis = getDistribution(i);
                    myGuess[i] = dis;
                }
                for (double[] vector : myGuess) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }
                    pw.println(arrayToCSV(vector));
                }
                pw.close();
                s.close();
                System.out.println("READY\t" + currentRequest + "\t" + currentWork + "/output/output.csv");
            } catch (InterruptedException e) {
                System.out.println("ABORTED\t" + currentRequest);
            } catch (Exception e) {
                System.err.println("Oops, " + e.getMessage());
                e.printStackTrace();
                System.out.println("FAILED\t" + currentRequest + "\t" + currentWork + "/output/output.csv");
            }
        }

        public void cancel() {
            interrupt();
        }

    }

}
